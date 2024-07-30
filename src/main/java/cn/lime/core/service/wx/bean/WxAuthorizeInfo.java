package cn.lime.core.service.wx.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: WxAuthorizeResult
 * @Description: 微信授权信息
 * @Author: Lime
 * @Date: 2023/6/9 12:02
 */
@Data
public class WxAuthorizeInfo {

    @JSONField(name = "openid")
    private String openId;
    @JSONField(name = "session_key")
    private String sessionKey;
    @JSONField(name = "unionid")
    private String unionId;
    @JSONField(name = "errcode")
    private Integer errorCode;
    @JSONField(name = "errmsg")
    private String errorMsg;
    @JSONField(name = "access_token")
    private String accessToken;

}
