package cn.lime.core._sms;

import cn.lime.core._common.BusinessException;
import cn.lime.core._common.ErrorCode;
import cn.lime.core._common.ThrowUtils;
import cn.lime.core._constant.RedisDb;
import cn.lime.core._sms.strategy.AliSmsService;
import cn.lime.core._sms.strategy.SmsStrategy;
import cn.lime.core._utils.RandomVerificationCodeUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: BasePhoneService
 * @Description: 手机号服务基类
 * @Author: Lime
 * @Date: 2023/12/18 16:10
 */
@Service
public class UniSmsService implements IUniSmsService{

    @Resource
    private AliSmsService aliPhoneService;
    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;
    /**
     * 短信超时时间 单位分钟
     */
    @Value("${basic-service.phone.cache-ttl:3}")
    private Integer messageTtl;
    private final List<SmsStrategy> smsStrategies;
    // 策略映射（启动时初始化）
    private Map<String, SmsStrategy> smsStrategyMap;

    public UniSmsService(List<SmsStrategy> smsStrategies) {
        this.smsStrategies = smsStrategies;
    }

    // 初始化策略映射
    // Spring Bean初始化后执行
    @PostConstruct
    public void initStrategyMap() {
        if (CollectionUtils.isEmpty(smsStrategies)) {
            smsStrategyMap = new HashMap<>();
            return;
        }
        smsStrategyMap = smsStrategies.stream()
                .collect(Collectors.toMap(SmsStrategy::getType, strategy -> strategy));
    }

    /**
     * 发送手机验证码
     * 根据配置选择使用用户中心统一发送验证码或机构自己配置
     */
    public void sendMessage(String mobilePhone,String type){
         // 核验缓存中是否有验证码，如果有验证码则不进行发送短信
        boolean exist = redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).hasKey(mobilePhone);
        ThrowUtils.throwIf(exist, ErrorCode.SMS_ERROR, "距上次发送短信验证码未满" + messageTtl + "min,您仍可使用旧验证码");
        // 验证码入缓存
        String code = RandomVerificationCodeUtils.nextCode();
        redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).opsForValue().set(mobilePhone, code, Duration.ofMinutes(messageTtl));
        // 发送
        SmsStrategy strategy = smsStrategyMap.get(type);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(strategy),ErrorCode.PARAMS_ERROR,"不支持的登录类型:"+type);
        try {
            strategy.sendMobilePhone(mobilePhone, code);
        }catch (Exception e){
            // 清空Redis已存储的
            redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).opsForValue().getAndDelete(mobilePhone);
            throw new BusinessException(ErrorCode.SMS_ERROR,"发送短信失败");
        }
    }


    public boolean checkCode(String mobilePhone, String code) {
        ThrowUtils.throwIf(!aliPhoneService.initSuccess() || redisTemplateMap.isEmpty(),ErrorCode.INIT_FAIL,"短信功能初始化失败");
        String cacheCode = redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).opsForValue().get(mobilePhone);
        return StringUtils.equals(cacheCode, code);
    }

}
