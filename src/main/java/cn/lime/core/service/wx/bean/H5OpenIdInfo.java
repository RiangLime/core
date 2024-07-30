package cn.lime.core.service.wx.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: H5AccessInfo
 * @Description: h5 accesstoken
 * @Author: Lime
 * @Date: 2023/12/1 16:15
 */
@Data
public class H5OpenIdInfo {

    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "expire_in")
    private int expireIn;

    @JSONField(name = "refresh_token")
    private String refreshToken;

    @JSONField(name = "openid")
    private String openId;

    @JSONField(name = "unionid")
    private String unionId;

    private String scope;

}
