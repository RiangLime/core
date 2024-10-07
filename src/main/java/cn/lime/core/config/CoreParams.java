package cn.lime.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CoreParams {

    @Value("${core.token.access-expire-millis:3600000}")
    private Long accessTokenExpires;
    @Value("${core.token.refresh-expire-hours:720}")
    private Integer refreshTokenExpireHours;

    // snowFlake
    @Value("${core.snowflake.datacenter-id:1}")
    private long datacenterId;
    @Value("${core.snowflake.machine-id:1}")
    private long machineId;

    // aes
    @Value("${core.aes.key:1234123412341234}")
    private String key;
    @Value("${core.aes.iv:1234123412341234}")
    private String iv;

    // ai
    @Value("${core.openai.url:}")
    private String aiUrl;
    @Value("${core.openai.api-key:}")
    private String aiKey;

    // ocr
    @Value("${core.ocr.ali.access-id:}")
    private String aliOcrAccessId;
    @Value("${core.ocr.ali.secret-id:}")
    private String aliOcrSecretId;
    @Value("${core.ocr.ali.endpoint:}")
    private String aliOcrEndPoint;

    // oss
    @Value("${core.oss.qiniu.token-expire:10}")
    private String qiNiuOssTokenExpire;
    @Value("${core.oss.qiniu.access-id:}")
    private String qiNiuOssAppId;
    @Value("${core.oss.qiniu.secret-id:}")
    private String qiNiuOssSecretId;

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

    // wx auth
    @Value("${core.wx-auth.h5.token-url:https://api.weixin.qq.com/sns/oauth2/access_token}")
    private String wxH5AuthTokenUrl;
    @Value("${core.wx-auth.mp.token-url:https://api.weixin.qq.com/cgi-bin/token}")
    private String wxMpAuthTokenUrl;
    @Value("${core.wx-auth.mp.openid-url:https://api.weixin.qq.com/sns/jscode2session}")
    private String wxMpAuthUserInfoUrl;
    @Value("${core.wx-auth.mp.phone-url:https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s}")
    private String wxMpAuthMobileUrl;
    @Value("${core.wx-auth.mp.share-qrcode-url:https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s}")
    private String wxMpUnlimitedQRCodeUrl;

    @Value("${core.wx.mp.app-id:}")
    private String wxMpAppId;
    @Value("${core.wx.mp.secret-id:}")
    private String wxMpSecretId;

    @Value("${core.wx.h5.app-id:}")
    private String wxH5AppId;
    @Value("${core.wx.h5.secret-id:}")
    private String wxH5SecretId;


    // token key
    @Value("${core.token.aes-key:ibviuziu18g48b1}")
    private String accessTokenEncodeKey;

    // file storage
    @Value("${core.file-storage.path:/home/ubuntu/uploads}")
    private String fileStoragePath;
    @Value("${core.file-storage.prefix:https://java.shop.ceagull.top/upload}")
    private String fileStorageUrlPrefix;

    // file storage
    @Value("${core.avatar-storage.path:/home/ubuntu/uploads_avatar}")
    private String avatarStoragePath;
    @Value("${core.avatar-storage.prefix:https://java.shop.ceagull.top/upload}")
    private String avatarStorageUrlPrefix;
    @Value("${core.avatar-storage.max-size-KB:500}")
    private Integer avatarMaxSizeKB;

}
