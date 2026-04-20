package cn.lime.core._sms.strategy;

import cn.lime.core._common.BusinessCatchableException;
import cn.lime.core._common.ErrorCode;
import cn.lime.core._common.ThrowUtils;
import cn.lime.core._sms.config.AliSmsProperties;
import cn.lime.core.module.entity.PhonemessageLog;
import cn.lime.core.service.db.PhonemessageLogService;
import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AliPhoneService
 * @Description: 阿里手机号服务
 * @Author: Lime
 * @Date: 2023/12/18 16:09
 */
@Service
@Slf4j
public class AliSmsService implements SmsStrategy {

    private boolean initSuccess;
    @Resource
    private AliSmsProperties aliSmsProperties;

    private com.aliyun.dysmsapi20170525.Client client;

    private static final String CODE_FORMAT = "{\"code\":\"%s\"}";

    @Resource
    private PhonemessageLogService phonemessageLogService;


    @PostConstruct
    public void checkInit() throws Exception {
        initSuccess = initSuccess();
        if (!initSuccess) {
            log.warn("[Init Ali Phone] 未检测到Ali 短信服务相关配置");
            return;
        }
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(aliSmsProperties.getAliPhoneAccessId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(aliSmsProperties.getAliPhoneSecretId());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = aliSmsProperties.getAliPhoneEndPoint();
        this.client = new com.aliyun.dysmsapi20170525.Client(config);
    }

    public boolean initSuccess() {
        return StringUtils.isNotEmpty(aliSmsProperties.getAliPhoneAccessId())
                && StringUtils.isNotEmpty(aliSmsProperties.getAliPhoneSecretId())
                && StringUtils.isNotEmpty(aliSmsProperties.getAliPhoneSignName())
                && StringUtils.isNotEmpty(aliSmsProperties.getAliPhoneEndPoint())
                && StringUtils.isNotEmpty(aliSmsProperties.getAliPhoneTemplateId());
    }

    public void sendMobilePhone(String mobilePhone, String code) throws Exception {
        ThrowUtils.throwIf(!initSuccess, ErrorCode.INIT_FAIL, "Ali短信功能初始化失败");
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(mobilePhone)
                .setSignName(aliSmsProperties.getAliPhoneSignName())
                .setTemplateCode(aliSmsProperties.getAliPhoneTemplateId())
                .setTemplateParam(String.format(CODE_FORMAT, code));
        try {
            SendSmsResponse response = client.sendSms(sendSmsRequest);
            log.info("调用阿里短信接口,RESP[{}]", JSON.toJSONString(response));
            if (!"OK".equals(response.getBody().getCode())) {
                throw new BusinessCatchableException(ErrorCode.SMS_ERROR, response.getBody().getMessage());
            }
            // 留痕
            phonemessageLogService.save(new PhonemessageLog(mobilePhone, response.getBody().message));
        } catch (BusinessCatchableException e) {
            log.error("发送短信服务失败,", e);
            throw e;
        } catch (Exception e) {
            log.error("发送短信失败", e);
            throw new BusinessCatchableException(ErrorCode.IO_ERROR, "发送短信服务失败");
        }

    }

    @Override
    public String getType() {
        return SmsTypeEnum.ALI.getVal();
    }
}
