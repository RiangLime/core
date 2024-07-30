package cn.lime.core.service.wx.auth;

import cn.hutool.http.HttpUtil;
import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.service.wx.bean.AccessTokenInfo;
import cn.lime.core.service.wx.bean.WxAuthorizeInfo;
import cn.lime.core.service.wx.bean.WxPhoneInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WxOuterService
 * @Description: 微信小程序授权服务
 * @Author: Lime
 * @Date: 2023/10/17 16:21
 */
@Service
public class WxMpOuterService {

    @Resource
    private CoreParams coreParams;

    @Resource
    private RestTemplate restTemplate;

    private static final OkHttpClient CLIENT = new OkHttpClient();


    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;

    public WxAuthorizeInfo getWxAuthorizeInfo(String appId, String secretId, String code) {
        Map<String, Object> paramMap = new HashMap<String, Object>() {
            {
                put("appid", appId);
                put("secret", secretId);
                put("js_code", code);
                put("grant_type", "authorization_code");
            }
        };
        String getResult = HttpUtil.get(coreParams.getWxMpAuthUserInfoUrl(), paramMap);
        WxAuthorizeInfo wxAuthorizeInfo = JSON.parseObject(getResult, WxAuthorizeInfo.class);
        ThrowUtils.throwIf(ObjectUtils.isNotEmpty(wxAuthorizeInfo.getErrorCode()) &&
                wxAuthorizeInfo.getErrorCode() != 0, ErrorCode.WX_OPENID_INTERFACE_ERROR, wxAuthorizeInfo.getErrorMsg());
        return wxAuthorizeInfo;
    }

    public WxPhoneInfo getWxPhoneInfo(String appId, String secretId, String code) throws BusinessException {
        AccessTokenInfo token = getAccessToken(appId, secretId);
        String url = String.format(coreParams.getWxMpAuthMobileUrl(), token.getAccessToken());

        JSONObject param = new JSONObject();
        param.put("code", code);
        //添加token
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //封装请求头
        org.springframework.http.HttpEntity<String> formEntity =
                new org.springframework.http.HttpEntity<>(JSON.toJSONString(param), headers);

        ResponseEntity<JSONObject> response2 = restTemplate.postForEntity(url, formEntity, JSONObject.class);
        System.out.println(response2.getBody());
        JSONObject respJson = JSON.parseObject(String.valueOf(response2.getBody()));
        ThrowUtils.throwIf((Integer) respJson.get("errcode") != 0,
                ErrorCode.WX_PHONE_INTERFACE_ERROR, respJson.get("errmsg").toString());
        return JSON.parseObject(respJson.get("phone_info").toString(), WxPhoneInfo.class);

    }


    public AccessTokenInfo getAccessToken(String appId, String secretId) {
        ThrowUtils.throwIf(redisTemplateMap.isEmpty(), ErrorCode.INIT_FAIL, "redis未正确配置");
        Object accessTokenJson = redisTemplateMap.get(RedisDb.WX_ACCESS_TOKEN.getVal()).opsForValue().get(appId + secretId);
        if (ObjectUtils.isNotEmpty(accessTokenJson)) {
            return JSON.parseObject(accessTokenJson.toString(), AccessTokenInfo.class);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>() {
            {
                put("appid", appId);
                put("secret", secretId);
                put("grant_type", "client_credential");
            }
        };
        String getResult = HttpUtil.get(coreParams.getWxMpAuthTokenUrl(), paramMap);
        redisTemplateMap.get(RedisDb.WX_ACCESS_TOKEN.getVal()).opsForValue().set(appId + secretId, getResult, Duration.ofSeconds(7000));
        return JSON.parseObject(getResult, AccessTokenInfo.class);
    }


}
