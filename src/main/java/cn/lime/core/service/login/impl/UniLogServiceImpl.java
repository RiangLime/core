package cn.lime.core.service.login.impl;

import cn.lime.core.aes.AesUtils;
import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import cn.lime.core.constant.*;
import cn.lime.core.module.bean.AppKeySecretInfo;
import cn.lime.core.module.bean.AuthInfo;
import cn.lime.core.module.dto.unidto.*;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.module.vo.LoginVo;
import cn.lime.core.module.vo.TokenCheckVo;
import cn.lime.core.service.db.LoginLogService;
import cn.lime.core.service.db.NicknamerepoService;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.db.UserthirdauthorizationService;
import cn.lime.core.service.login.UniLogService;
import cn.lime.core.service.phone.AliPhoneService;
import cn.lime.core.service.phone.BasePhoneService;
import cn.lime.core.service.wx.auth.WxH5OuterService;
import cn.lime.core.service.wx.auth.WxMpOuterService;
import cn.lime.core.service.wx.bean.H5OpenIdInfo;
import cn.lime.core.service.wx.bean.WxAuthorizeInfo;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.core.token.AccessTokenHandler;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;


/**
 * @ClassName: BizPersonalService
 * @Description: 用户管理业务类
 * @Author: Lime
 * @Date: 2023/12/11 13:40
 */
@Service
public class UniLogServiceImpl implements UniLogService {

    @Resource
    private UserthirdauthorizationService userthirdauthorizationService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private UserService userService;
    @Resource
    private WxH5OuterService wxH5OuterService;
    @Resource
    private WxMpOuterService wxMpOuterService;
    @Resource
    private NicknamerepoService nicknamerepoService;
    @Resource
    private BasePhoneService mobileService;
    @Resource
    protected Map<Integer, StringRedisTemplate> redisTemplateMap;
    @Resource
    private SnowFlakeGenerator ids;
    @Resource
    private AccessTokenHandler accessTokenHandler;
    @Resource
    private CoreParams coreParams;
    @Resource
    private AesUtils aesUtils;


    @Override
    public LoginVo easyLogin(UniEasyLoginDto dto) {
        LoginVo vo = null;
        if (dto instanceof WxEasyLoginDto) {
            vo = easyLoginWx((WxEasyLoginDto) dto);
        } else if (dto instanceof PhoneEasyLoginDto) {
            vo = easyLoginPhone((PhoneEasyLoginDto) dto);
        } else if (dto instanceof FirebaseEasyLoginDto) {
            vo = easyLoginFirebase((FirebaseEasyLoginDto) dto);
        } else if (dto instanceof AccountLoginDto) {
            vo = loginAccount((AccountLoginDto) dto);
        } else {
            throw new BusinessException(ErrorCode.UNSUPPORTED_METHOD);
        }
        return vo;
    }

    private LoginVo easyLoginWx(WxEasyLoginDto dto) {
        // 获取平台标志 1Mp 2H5 3...
        Integer platform = ReqThreadLocal.getInfo().getPlatform();
        // 获取OPENID和unionID
        String openId = "";
        String unionId = "";

        AppKeySecretInfo appKeySecretInfo = platform == 1 ?
                new AppKeySecretInfo(coreParams.getWxMpAppId(), coreParams.getWxMpSecretId()) :
                new AppKeySecretInfo(coreParams.getWxH5AppId(), coreParams.getWxH5SecretId());
        if (platform.equals(PlatformEnum.MP.getVal())) {
            // 获取OPENID
            WxAuthorizeInfo authorizeInfo = wxMpOuterService.getWxAuthorizeInfo(appKeySecretInfo.getAppKey()
                    , appKeySecretInfo.getAppSecret(), dto.getOpenIdCode());
            openId = authorizeInfo.getOpenId();
            unionId = authorizeInfo.getUnionId();
        } else if (platform.equals(PlatformEnum.H5.getVal())) {
            H5OpenIdInfo authorizeInfo = wxH5OuterService.getH5OpenId(appKeySecretInfo.getAppKey()
                    , appKeySecretInfo.getAppSecret(), dto.getOpenIdCode());
            openId = authorizeInfo.getOpenId();
            unionId = authorizeInfo.getUnionId();
        } else {
            throw new BusinessException(ErrorCode.UNSUPPORTED_METHOD, "平台标志异常,仅可为1或2");
        }
        ThrowUtils.throwIf(StringUtils.isEmpty(openId) || StringUtils.isEmpty(unionId), ErrorCode.WX_OPENID_INTERFACE_ERROR, "获取OpenID失败");

        User personnel = null;
        boolean userExistFlag = userthirdauthorizationService
                .lambdaQuery()
                .eq(Userthirdauthorization::getThirdFirstTag, unionId).exists();
        // 用户存在 进行登录
        if (userExistFlag) {
            // 以前注册过的用户ID
            personnel = userService.getById(userthirdauthorizationService
                    .lambdaQuery()
                    .eq(Userthirdauthorization::getThirdFirstTag, unionId).one().getPersonnelId());
            ThrowUtils.throwIf(ObjectUtils.isEmpty(personnel), ErrorCode.NOT_FOUND_ERROR, "无法根据微信UNION_ID查询到该用户信息");
            // 新的OPENID不存在,需要插入
            if (!userthirdauthorizationService
                    .lambdaQuery()
                    .eq(Userthirdauthorization::getThirdFirstTag, unionId)
                    .eq(Userthirdauthorization::getThirdSecondTag, openId).exists()) {
                Userthirdauthorization userthirdauthorization = new Userthirdauthorization(personnel.getUserId(),
                        platform, ThirdAuthorizationType.Wechat.getVal(), unionId, openId);
                ThrowUtils.throwIf(!userthirdauthorizationService.save(userthirdauthorization),
                        ErrorCode.INSERT_ERROR, "插入用户第三方授权信息失败");
            }
        }
        // 用户不存在进行注册
        else {
            // 用户基本信息注册 以及各个三大实体的关联信息
            String nickname = nicknamerepoService.getRandomNick();
            personnel = new User(ids.nextId(), UUID.randomUUID().toString(), nickname);
            ThrowUtils.throwIf(!userService.save(personnel), ErrorCode.INSERT_ERROR, "插入新用户信息失败");
            // 存储第三方授权信息
            Userthirdauthorization userthirdauthorization = new Userthirdauthorization(personnel.getUserId(),
                    platform, ThirdAuthorizationType.Wechat.getVal(), unionId, openId);
            ThrowUtils.throwIf(!userthirdauthorizationService.save(userthirdauthorization),
                    ErrorCode.INSERT_ERROR, "插入用户第三方授权信息失败");
        }
        // 登录
        LoginVo loginVo = generateToken(personnel, platform, ReqThreadLocal.getInfo().getIp());
        loginVo.setIsBindPhone(StringUtils.isNotEmpty(personnel.getPhone()));
        loginVo.setIsNew(userExistFlag);
        return loginVo;
    }

    private LoginVo easyLoginFirebase(FirebaseEasyLoginDto dto) {
        // 获取平台标志 1Mp 2H5 3...
        Integer platform = ReqThreadLocal.getInfo().getPlatform();
        Userthirdauthorization userthirdauthorization = userthirdauthorizationService
                .lambdaQuery()
                .eq(Userthirdauthorization::getThirdType, dto.getThirdLoginType())
                .eq(Userthirdauthorization::getThirdFirstTag, dto.getUid()).one();
        User personnel = ObjectUtils.isEmpty(userthirdauthorization) ? null :
                userService.getById(userthirdauthorization.getPersonnelId());
        // 如果为空 要进行注册
        if (ObjectUtils.isEmpty(userthirdauthorization)) {
            String nickname = nicknamerepoService.getRandomNick();
            personnel = new User(ids.nextId(), UUID.randomUUID().toString(), nickname);
            ThrowUtils.throwIf(!userService.save(personnel), ErrorCode.INSERT_ERROR, "插入新用户信息失败");
            // 存储第三方授权信息
            Userthirdauthorization bean = new Userthirdauthorization(personnel.getUserId(),
                    platform, dto.getThirdLoginType(), dto.getUid(), null);
            ThrowUtils.throwIf(!userthirdauthorizationService.save(bean),
                    ErrorCode.INSERT_ERROR, "插入用户第三方授权信息失败");
        }
        ThrowUtils.throwIf(ObjectUtils.isEmpty(personnel), ErrorCode.NOT_FOUND_ERROR, "无法进行一键登录,无该用户信息");
        // 登录
        LoginVo loginVo = generateToken(personnel, platform, ReqThreadLocal.getInfo().getIp());
        loginVo.setIsBindPhone(StringUtils.isNotEmpty(personnel.getPhone()));
        loginVo.setIsNew(ObjectUtils.isEmpty(userthirdauthorization));
        return loginVo;
    }


    private LoginVo easyLoginPhone(PhoneEasyLoginDto dto) {
        // 验证手机验证码
        ThrowUtils.throwIf(!mobileService.checkCode(dto.getPhone(), dto.getCode()), ErrorCode.MOBILE_PHONE_MESSAGE_CODE_ERROR);
        User personnel = userService.lambdaQuery().eq(User::getPhone, dto.getPhone()).one();
        // 获取平台标志 1Mp 2H5 3...
        Integer platform = ReqThreadLocal.getInfo().getPlatform();
        boolean userExistFlag = ObjectUtils.isNotEmpty(personnel);
        // 无该用户,需要进行注册
        if (!userExistFlag) {
            // 用户基本信息注册 以及各个三大实体的关联信息
            String nickname = nicknamerepoService.getRandomNick();
            personnel = new User(ids.nextId(), UUID.randomUUID().toString(), nickname);
            ThrowUtils.throwIf(!userService.save(personnel), ErrorCode.INSERT_ERROR, "插入新用户信息失败");
        }
        // 进行登录
        LoginVo loginVo = generateToken(personnel, platform, ReqThreadLocal.getInfo().getIp());
        loginVo.setIsBindPhone(StringUtils.isNotEmpty(personnel.getPhone()));
        loginVo.setIsNew(userExistFlag);
        return loginVo;
    }

    private LoginVo loginAccount(AccountLoginDto dto) {
        User user = userService.lambdaQuery().eq(User::getAccount, dto.getAccount()).one();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(user), ErrorCode.NOT_FOUND_ERROR, "该账号不存在");
        try {
            String decode = aesUtils.decrypt(dto.getPwd());
            ThrowUtils.throwIf(!user.getPassword().equals(decode), ErrorCode.PWD_ERROR, "用户密码错误");
        }catch (Exception e){
            throw new BusinessException(ErrorCode.IO_ERROR,"解密用户密码失败");
        }

        Integer platform = ReqThreadLocal.getInfo().getPlatform();
        // 进行登录
        LoginVo loginVo = generateToken(user, platform,
                ReqThreadLocal.getInfo().getIp());
        loginVo.setIsBindPhone(StringUtils.isNotEmpty(user.getPhone()));
        loginVo.setIsNew(false);
        return loginVo;
    }

    @Override
    public LoginVo generateToken(User user, Integer platform, String ip) {
        ThrowUtils.throwIf(!userService.lambdaQuery().eq(User::getUserId, user.getUserId()).exists(),
                ErrorCode.NOT_FOUND_ERROR, "未找到相关账号信息");
        String accessToken = accessTokenHandler.getToken(user, 1, 1);
        String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
        return doLogin(accessToken, refreshToken, user.getUserId(), platform, ip);

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
        return accessTokenHandler.getToken(user, 1, 1);
    }

    @Override
    public TokenCheckVo checkJwtAccessToken(String accessToken) {
        return accessTokenHandler.checkJwtAccessToken(accessToken);
    }
}
