package cn.lime.core.service.phone;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.module.entity.PhonemessageLog;
import cn.lime.core.service.db.PhonemessageLogService;
import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: AliPhoneService
 * @Description: 阿里手机号服务
 * @Author: Lime
 * @Date: 2023/12/18 16:09
 */
@Service
@Slf4j
public class AliPhoneService implements InitializingBean {

    private boolean initSuccess;
    @Resource
    private CoreParams coreParams;

    private com.aliyun.dysmsapi20170525.Client client;

    private static final String CODE_FORMAT = "{\"code\":\"%s\"}";

    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;
    @Resource
    private PhonemessageLogService phonemessageLogService;

    public void sendMobilePhone(String mobilePhone, String code) {
        ThrowUtils.throwIf(!initSuccess,ErrorCode.INIT_FAIL,"Ali短信功能初始化失败");
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(mobilePhone)
                .setSignName(coreParams.getAliPhoneSignName())
                .setTemplateCode(coreParams.getAliPhoneTemplateId())
                .setTemplateParam(String.format(CODE_FORMAT, code));
        try {
            SendSmsResponse response = client.sendSms(sendSmsRequest);
            log.info("调用阿里短信接口,RESP[{}]", JSON.toJSONString(response));
            if (!"OK".equals(response.getBody().getCode())) {
                throw new BusinessException(ErrorCode.MOBILE_PHONE_MESSAGE_ERROR, response.getBody().getMessage());
            }
            // 留痕
            phonemessageLogService.save(new PhonemessageLog(mobilePhone, response.getBody().message));
        } catch (BusinessException e) {
            redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).opsForValue().getAndDelete(mobilePhone);
            throw e;
        } catch (Exception e) {
            redisTemplateMap.get(RedisDb.VERIFICATION.getVal()).opsForValue().getAndDelete(mobilePhone);
            throw new BusinessException(ErrorCode.IO_ERROR, "发送短信服务失败");
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initSuccess = initSuccess();
        if (!initSuccess) {
            log.warn("[Init Ali Phone] 未检测到Ali 短信服务相关配置");
            return;
        }
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(coreParams.getAliPhoneAccessId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(coreParams.getAliPhoneSecretId());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = coreParams.getAliPhoneEndPoint();
        this.client = new com.aliyun.dysmsapi20170525.Client(config);
    }

    public boolean initSuccess() {
        return StringUtils.isNotEmpty(coreParams.getAliPhoneAccessId())
                && StringUtils.isNotEmpty(coreParams.getAliPhoneSecretId())
                && StringUtils.isNotEmpty(coreParams.getAliPhoneSignName())
                && StringUtils.isNotEmpty(coreParams.getAliPhoneEndPoint())
                && StringUtils.isNotEmpty(coreParams.getAliPhoneTemplateId());
    }

}
