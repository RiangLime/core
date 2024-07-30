package cn.lime.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CoreParams {

    // snowFlake
    private long datacenterId;
    private long machineId;

    // aes
    private String key;
    private String iv;

    // ai
    private String aiUrl;
    private String aiKey;

    // ocr
    private String aliOcrAccessId;
    private String aliOcrSecretId;
    private String aliOcrEndPoint;

    // oss
    private String qiNiuOssTokenExpire;
    private String qiNiuOssAppId;
    private String qiNiuOssSecretId;

    // phone
    private String aliPhoneAccessId;
    private String aliPhoneSecretId;
    private String aliPhoneEndPoint;
    private String aliPhoneTemplateId;
    private String aliPhoneSignName;

    // wx auth
    private String wxH5AuthTokenUrl;
    private String wxMpAuthTokenUrl;
    private String wxMpAuthUserInfoUrl;
    private String wxMpAuthMobileUrl;

    // wx pay
    private String wxPayMerchantId;
    private String wxPayPrivateKeyPath;
    private String wxPayMerchantSerialNumber;
    private String wxPayApiV3Key;
    private String wxPayApId;
    private String wxPayCertificatePath;
    private String wxPayNotifyUrlPrefix;

    // token key
    private String accessTokenEncodeKey;

}