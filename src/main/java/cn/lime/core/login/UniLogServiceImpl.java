package cn.lime.core._login;

import cn.lime.core._login.strategy.LoginStrategy;
import cn.lime.core._login.strategy.LoginStrategyRes;
import cn.lime.core._common.BusinessException;
import cn.lime.core._common.ErrorCode;
import cn.lime.core._common.ThrowUtils;
import cn.lime.core._constant.RedisDb;
import cn.lime.core.module.dto.unidto.*;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.vo.LoginVo;
import cn.lime.core.module.vo.TokenCheckVo;
import cn.lime.core.service.db.LoginLogService;
import cn.lime.core.service.db.UserService;
import cn.lime.core._threadlocal.ReqThreadLocal;
import cn.lime.core._login.token.AccessTokenHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName: BizPersonalService
 * @Description: 用户管理业务类
 * @Author: Lime
 * @Date: 2023/12/11 13:40
 */
@Service
@Slf4j
public class UniLogServiceImpl implements UniLogService {

    @Resource
    private LoginLogService loginLogService;
    @Resource
    private UserService userService;
    @Resource
    protected Map<Integer, StringRedisTemplate> redisTemplateMap;
    @Resource
    private AccessTokenHandler accessTokenHandler;
    private final List<LoginStrategy> loginStrategies;
    // 策略映射（启动时初始化）
    private Map<String, LoginStrategy> loginStrategyMap;

    public UniLogServiceImpl(List<LoginStrategy> loginStrategies) {
        this.loginStrategies = loginStrategies;
    }

    // 初始化策略映射
    // Spring Bean初始化后执行
    @PostConstruct
    public void initStrategyMap() {
        if (CollectionUtils.isEmpty(loginStrategies)) {
            loginStrategyMap = new HashMap<>();
            return;
        }
        loginStrategyMap = loginStrategies.stream()
                .collect(Collectors.toMap(LoginStrategy::getType, strategy -> strategy));
    }

    @Override
    public LoginVo easyLogin(UniEasyLoginDto dto) {
        // 获取登录策略
        LoginStrategy strategy = loginStrategyMap.get(dto.getLoginType());
        ThrowUtils.throwIf(ObjectUtils.isEmpty(strategy),ErrorCode.PARAMS_ERROR,"不支持的登录类型:"+dto.getLoginType());
        // 执行数据库校验登录
        LoginStrategyRes user = strategy.loginCheck(dto);
        // 生成Token进行登录
        String accessToken = accessTokenHandler.getToken(user.getUserId(), user.getUserRole(), 1);
        String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");

        LoginVo vo = doLogin(accessToken,refreshToken,user.getUserId(),ReqThreadLocal.getInfo().getPlatform(), ReqThreadLocal.getInfo().getIp());
        return vo;
    }

    private LoginVo doLogin(String token, String refreshToken, Long userId, Integer platform, String ip) {
        // 删除该机构该APP该用户的所有其他REFRESH token
        Set<String> redisKey;
        redisKey = redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue().getOperations()
                .keys("refresh_" + userId + "_*");
        if (!CollectionUtils.isEmpty(redisKey)) {
            redisKey.forEach(key -> redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue().getAndDelete(key));
        }
        String refreshKey = "refresh_" + userId + "_" + refreshToken;
        // 存放新的REFRESH token
        redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue()
                .set(refreshKey, String.valueOf(userId), Duration.ofHours(accessTokenHandler.getRefreshTokenExpire()));
        Long refreshTtl = redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue().getOperations().getExpire(refreshKey);
        loginLogService.appendLog(userId, ip, platform);
        return new LoginVo(userId, token, refreshToken, accessTokenHandler.getAccessTokenExpire() / 1000, refreshTtl);
    }



    @Override
    public void logout() {
        Set<String> redisKey;
        // 删除REFRESH token
        redisKey = redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue().getOperations()
                .keys("refresh_" + ReqThreadLocal.getInfo().getUserId() + "_*");
        if (!CollectionUtils.isEmpty(redisKey)) {
            redisKey.forEach(key -> redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue().getAndDelete(key));
        }
    }

    @Override
    public String refreshToken(String refreshToken) {
        Set<String> redisKey;
        redisKey = redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue().getOperations()
                .keys("refresh_*_" + refreshToken);
        ThrowUtils.throwIf(CollectionUtils.isEmpty(redisKey), ErrorCode.INVALID_REFRESH_TOKEN);
        String uidStr = redisTemplateMap.get(RedisDb.TOKEN.getVal()).opsForValue().get(new ArrayList<>(redisKey).get(0));
        long uid;
        try {
            ThrowUtils.throwIf(ObjectUtils.isEmpty(uidStr), ErrorCode.IO_ERROR, "根据REFRESH TOKEN解析uid异常");
            uid = Long.parseLong(uidStr);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.IO_ERROR, "根据REFRESH TOKEN解析uid异常");
        }
        // 重新生成token
        User user = userService.getById(uid);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(user), ErrorCode.NOT_FOUND_ERROR);
        return accessTokenHandler.getToken(user, user.getRole(), 1);
    }

    @Override
    public TokenCheckVo checkJwtAccessToken(String accessToken) {
        return accessTokenHandler.checkJwtAccessToken(accessToken);
    }
}
