package cn.lime.core._wx.auth;

import cn.hutool.http.HttpUtil;
import cn.lime.core._config.WxProperties;
import cn.lime.core._wx.bean.H5OpenIdInfo;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WxH5OuterService
 * @Description: 微信H5授权服务
 * @Author: Lime
 * @Date: 2023/12/1 15:32
 */
@Service
public class WxH5OuterService {

    @Resource
    private WxProperties wxProperties;


    public H5OpenIdInfo getH5OpenId(String appId, String secretId, String code) {
        Map<String, Object> paramMap = new HashMap<String, Object>() {
            {
                put("appid", appId);
                put("secret", secretId);
                put("code", code);
                put("grant_type", "authorization_code");
            }
        };
        String getResult = HttpUtil.get(wxProperties.getWxH5AuthTokenUrl(), paramMap);
        return JSON.parseObject(getResult, H5OpenIdInfo.class);
    }

}
