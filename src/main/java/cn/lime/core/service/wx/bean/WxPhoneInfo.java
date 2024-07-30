package cn.lime.core.service.wx.bean;

import lombok.Data;

/**
 * @ClassName: WxPhoneInfo
 * @Description: 微信手机号信息实体
 * @Author: Lime
 * @Date: 2023/10/17 16:24
 */
@Data
public class WxPhoneInfo {

    private String phoneNumber;

    private String purePhoneNumber;

    private String countryCode;
}
