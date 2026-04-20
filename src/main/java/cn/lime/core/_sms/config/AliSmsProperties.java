package cn.lime.core._sms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: PhoneProperties
 * @Description: 手机号短信配置信息
 * @Author: riang
 * @Date: 2026/4/20 17:45
 */
@Data
@Component
public class AliSmsProperties {
    // phone
    @Value("${core.sms.ali.access-id:}")
    private String aliPhoneAccessId;
    @Value("${core.sms.ali.secret-id:}")
    private String aliPhoneSecretId;
    @Value("${core.sms.ali.endpoint:}")
    private String aliPhoneEndPoint;
    @Value("${core.sms.ali.template-id:}")
    private String aliPhoneTemplateId;
    @Value("${core.sms.ali.sign:}")
    private String aliPhoneSignName;
}