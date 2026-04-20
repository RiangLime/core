package cn.lime.core.service.wx.auth;

import cn.hutool.http.HttpUtil;
import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.constant.WxSendDeliverApiLogisticTypeEnum;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.module.entity.WxDeliverList;
import cn.lime.core.service.db.UserthirdauthorizationService;
import cn.lime.core.service.wx.bean.AccessTokenInfo;
import cn.lime.core.service.wx.bean.WxAuthorizeInfo;
import cn.lime.core.service.wx.bean.WxPhoneInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @ClassName: WxOuterService
 * @Description: 微信小程序授权服务
 * @Author: Lime
 * @Date: 2023/10/17 16:21
 */
@Service
@Slf4j
public class WxMpOuterService {

    @Resource
    private CoreParams coreParams;

    @Resource
    private RestTemplate restTemplate;

    private static final OkHttpClient CLIENT = new OkHttpClient();


    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;
    @Resource
    private UserthirdauthorizationService userthirdauthorizationService;

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

    public String getShareCode(String appId, String secretId, String page, String scene) {
        return getShareCode(appId, secretId, page, scene, true, "release");
    }

    public String getShareCode(String appId, String secretId, String page, String scene, Boolean checkPath, String env) {
        AccessTokenInfo token = getAccessToken(appId, secretId);
        String url = String.format(coreParams.getWxMpUnlimitedQRCodeUrl(), token.getAccessToken());

        JSONObject param = new JSONObject();
        param.put("page", page);
        param.put("scene", scene);
        param.put("check_path", checkPath);
        param.put("env_version", env);
        //添加token
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //封装请求头
        org.springframework.http.HttpEntity<String> formEntity =
                new org.springframework.http.HttpEntity<>(JSON.toJSONString(param), headers);

        ResponseEntity<byte[]> response2 = restTemplate.exchange(url, HttpMethod.POST, formEntity, byte[].class);
        HttpHeaders rspHeaders = response2.getHeaders();
        String rspContentType = rspHeaders.getContentType().toString();

        if (rspContentType.equals("image/jpeg")) {
            // 处理JPEG图片响应
            byte[] responseBody = response2.getBody();
            return Base64.getEncoder().encodeToString(responseBody);
        } else {
            String jsonString = new String(response2.getBody(), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            log.info("code:" + (Integer) jsonObject.get("errcode"));

            // accessToken过期
            if ((Integer) jsonObject.get("errcode") == 40001) {
                redisTemplateMap.get(RedisDb.WX_ACCESS_TOKEN.getVal()).opsForValue().getAndDelete(appId + secretId);
                throw new BusinessException(ErrorCode.WX_PHONE_INTERFACE_ERROR, "accessToken已过期," + jsonObject.get("errmsg").toString());
            } else {
                throw new BusinessException(ErrorCode.WX_PHONE_INTERFACE_ERROR, jsonObject.get("errmsg").toString());
            }
        }
    }

    public void sendMessageToUser(String appId, String secretId, String templateId, String page, String openId, JSONObject jsonObj) {
        sendMessageToUser(appId, secretId, templateId, page, openId, jsonObj, "formal", "zh_CN");
    }

    public void sendMessageToUser(String appId, String secretId, String templateId, String page, String openId, JSONObject jsonObj, String env) {
        sendMessageToUser(appId, secretId, templateId, page, openId, jsonObj, env, "zh_CN");
    }


    public void sendMessageToUser(String appId, String secretId, String templateId, String page, String openId, JSONObject jsonObj, String mpState, String lang) {
        AccessTokenInfo token = getAccessToken(appId, secretId);
        String url = String.format(coreParams.getWxMpSendMessageUrl(), token.getAccessToken());


        JSONObject param = new JSONObject();
        param.put("template_id", templateId);
        param.put("page", page);
        param.put("touser", openId);
        param.put("data", jsonObj);
        log.info("[SEND USER] param: {}", param.toJSONString());
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
    }


    public void uploadDeliverInfoToWx(String appId, String secretId, Long userId, String merchId, String outTradeNo, WxSendDeliverApiLogisticTypeEnum logisticsType,
                                      String itemDesc, String deliverId, String expressCompany, String receiverContact) {
        ThrowUtils.throwIf(itemDesc.length() > 100, ErrorCode.PARAMS_ERROR, "商品信息过长，请控制在100以下");
        String openId = userthirdauthorizationService.getUserWxOpenId(userId);
        ThrowUtils.throwIf(StringUtils.isEmpty(openId), ErrorCode.PARAMS_ERROR, "用户ID:" + userId + "无openId,请核验订单信息");

        // 最外层请求体
        JSONObject param = new JSONObject();
        // 订单，需要上传物流信息的订单
        JSONObject orderKey = new JSONObject();
        // 固定使用使用下单商户号和商户侧单号
        orderKey.put("order_number_type", 1);
        orderKey.put("mchid", merchId);


        orderKey.put("out_trade_no", outTradeNo);


        param.put("order_key", orderKey);

        param.put("logistics_type", logisticsType.getVal());
        // 默认统一发货
        param.put("delivery_mode", 1);
        // 物流信息列表
        JSONArray shipList = new JSONArray();
        JSONObject ship = new JSONObject();
        if (logisticsType.equals(WxSendDeliverApiLogisticTypeEnum.DELIVER)) {
            ship.put("tracking_no", deliverId);
            ship.put("express_company", expressCompany);
        }
        ship.put("item_desc", itemDesc);
        JSONObject contactorInfo = new JSONObject();
        if (logisticsType.equals(WxSendDeliverApiLogisticTypeEnum.DELIVER)) {
            contactorInfo.put("receiver_contact", receiverContact);
        }
        ship.put("contact", contactorInfo);
        shipList.add(ship);
        param.put("shipping_list", shipList);

        String beijingTime = OffsetDateTime.now(ZoneId.of("Asia/Shanghai")).toString();
        param.put("upload_time", beijingTime);

        JSONObject payer = new JSONObject();
        payer.put("openid", openId);
        param.put("payer", payer);

        // 构造请求体结束
        // 发送请求
        AccessTokenInfo token = getAccessToken(appId, secretId);
        String url = String.format(coreParams.getWxSendDeliverInfoUrl(), token.getAccessToken());
        log.info("[SEND WX DELIVER INFO] param: {}", param.toJSONString());
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
                ErrorCode.WX_SEND_DELIVER_INFO_ERROR, respJson.get("errmsg").toString());
    }

    public List<WxDeliverList> getWxDeliverList(String appId, String secretId) {
        // 构造请求体结束
        // 发送请求
        JSONObject param = new JSONObject();
        AccessTokenInfo token = getAccessToken(appId, secretId);
        String url = String.format(coreParams.getWxDeliverInfoUrl(), token.getAccessToken());
        log.info("[SEND WX DELIVER INFO] param: {}", param.toJSONString());
        //添加token
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //封装请求头
        org.springframework.http.HttpEntity<String> formEntity =
                new org.springframework.http.HttpEntity<>(JSON.toJSONString(param), headers);

        restTemplate.getMessageConverters().removeIf(converter ->
                converter instanceof org.springframework.http.converter.StringHttpMessageConverter
        );
        restTemplate.getMessageConverters().add(0, new org.springframework.http.converter.StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<String> response = restTemplate.postForEntity(url, formEntity, String.class);
        String responseBody = response.getBody();
        JSONObject respJson = JSON.parseObject(String.valueOf(responseBody));
        ThrowUtils.throwIf((Integer) respJson.get("errcode") != 0,
                ErrorCode.WX_SEND_DELIVER_INFO_ERROR, "微信接口错误");

        // 解析数据
        JSONArray jsonArray = respJson.getJSONArray("delivery_list");
        List<WxDeliverList> res = new LinkedList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            WxDeliverList bean = new WxDeliverList(jsonObject.getString("delivery_id"), jsonObject.getString("delivery_name"));
            res.add(bean);
        }
        return res;
    }

}
