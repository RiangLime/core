package cn.lime.core._config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WxProperties
 * @Description: 微信外部接口相关配置
 * @Author: riang
 * @Date: 2026/4/20 17:46
 */
@Data
@Component
public class WxProperties {
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
    @Value("${core.wx-auth.mp.send-message-url:https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=%s}")
    private String wxMpSendMessageUrl;
    @Value("${core.wx-auth.mp.send-message-template-id:DIevvAANm4WOiBI8iS3I8fsEMf0oGk6yP51JBqzG0Zg}")
    private String wxMpSendMessageTemplateId;
    @Value("${core.wx-auth.mp.send-message-page:pages/report/bind1step/index?code=}")
    private String wxMpSendMessagePage;
    @Value("${core.wx-auth.mp.send-message-env:formal}")
    private String wxMpSendMessageEnv;
    @Value("${core.wx-auth.send-deliver-info-url:https://api.weixin.qq.com/wxa/sec/order/upload_shipping_info?access_token=%s}")
    private String wxSendDeliverInfoUrl;
    @Value("${core.wx-auth-deliver-info-url:https://api.weixin.qq.com/cgi-bin/express/delivery/open_msg/get_delivery_list?access_token=%s}")
    private String wxDeliverInfoUrl;
    @Value("${core.auto-update-wx-deliver-info:true}")
    private Boolean autoUpdateWxDeliverInfo;


    @Value("${core.wx.mp.app-id:}")
    private String wxMpAppId;
    @Value("${core.wx.mp.secret-id:}")
    private String wxMpSecretId;

    @Value("${core.wx.h5.app-id:}")
    private String wxH5AppId;
    @Value("${core.wx.h5.secret-id:}")
    private String wxH5SecretId;
}