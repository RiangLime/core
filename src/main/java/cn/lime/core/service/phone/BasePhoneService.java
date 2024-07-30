package cn.lime.core.service.phone;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.utils.RandomVerificationCodeUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

/**
 * @ClassName: BasePhoneService
 * @Description: 手机号服务基类
 * @Author: Lime
 * @Date: 2023/12/18 16:10
 */
@Service
public class BasePhoneService {

    @Resource
    private AliPhoneService aliPhoneService;
    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;
    /**
     * 短信超时时间 单位分钟
     */
    @Value("${basic-service.phone.cache-ttl:3}")
    private Integer messageTtl;

    /**
     * 发送手机验证码
     * 根据配置选择使用用户中心统一发送验证码或机构自己配置
     */
    public void sendMessage(String mobilePhone){

        // 核验缓存中是否有验证码，如果有验证码则不进行发送短信
        boolean exist = Boolean.TRUE.equals(redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).hasKey(mobilePhone));
        ThrowUtils.throwIf(exist, ErrorCode.MOBILE_PHONE_MESSAGE_ERROR, "距上次发送短信验证码未满" + messageTtl + "min,您仍可使用旧验证码");
        // 验证码入缓存
        String code = RandomVerificationCodeUtils.nextCode();
        redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).opsForValue().set(mobilePhone, code, Duration.ofMinutes(messageTtl));
        // 发送
        aliPhoneService.sendMobilePhone(mobilePhone,code);
    }


    public boolean checkCode(String mobilePhone, String code) {
        String cacheCode = redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).opsForValue().get(mobilePhone);
        return StringUtils.equals(cacheCode, code);
    }

}
