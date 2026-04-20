package cn.lime.core._login.strategy;

import cn.lime.core._common.BusinessException;
import cn.lime.core._common.ErrorCode;
import cn.lime.core._common.ThrowUtils;
import cn.lime.core._config.WxProperties;
import cn.lime.core._constant.PlatformEnum;
import cn.lime.core._constant.ThirdAuthorizationType;
import cn.lime.core.module.bean.AppKeySecretInfo;
import cn.lime.core.module.dto.unidto.UniEasyLoginDto;
import cn.lime.core.module.dto.unidto.WxEasyLoginDto;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.service.db.NicknamerepoService;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.db.UserthirdauthorizationService;
import cn.lime.core._wx.auth.WxH5OuterService;
import cn.lime.core._wx.auth.WxMpOuterService;
import cn.lime.core._wx.bean.H5OpenIdInfo;
import cn.lime.core._wx.bean.WxAuthorizeInfo;
import cn.lime.core._snowflake.SnowFlakeGenerator;
import cn.lime.core._threadlocal.ReqThreadLocal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName: WxMpLoginStrategy
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/4/20 16:13
 */
@Service
@Slf4j
public class WxLoginStrategy implements LoginStrategy{
    @Resource
    private WxProperties wxProperties;
    @Resource
    private WxMpOuterService wxMpOuterService;
    @Resource
    private WxH5OuterService wxH5OuterService;
    @Resource
    private NicknamerepoService nicknamerepoService;
    @Resource
    private UserthirdauthorizationService userthirdauthorizationService;
    @Resource
    private SnowFlakeGenerator ids;
    @Resource
    private UserService userService;

    @Override
    public LoginStrategyRes loginCheck(UniEasyLoginDto baseDto) {
        WxEasyLoginDto dto = (WxEasyLoginDto) baseDto;
        // 获取平台标志 1Mp 2H5 3...
        Integer platform = ReqThreadLocal.getInfo().getPlatform();
        // 获取OPENID和unionID
        String openId = "";
        String unionId = "";

        AppKeySecretInfo appKeySecretInfo = platform == 1 ?
                new AppKeySecretInfo(wxProperties.getWxMpAppId(), wxProperties.getWxMpSecretId()) :
                new AppKeySecretInfo(wxProperties.getWxH5AppId(), wxProperties.getWxH5SecretId());
        if (platform.equals(PlatformEnum.MP.getVal())) {
            // 获取OPENID
            WxAuthorizeInfo authorizeInfo = wxMpOuterService.getWxAuthorizeInfo(appKeySecretInfo.getAppKey()
                    , appKeySecretInfo.getAppSecret(), dto.getOpenIdCode());
            openId = authorizeInfo.getOpenId();
            unionId = authorizeInfo.getUnionId();
        } else if (platform.equals(PlatformEnum.H5.getVal())) {
            H5OpenIdInfo authorizeInfo = wxH5OuterService.getH5OpenId(appKeySecretInfo.getAppKey()
                    , appKeySecretInfo.getAppSecret(), dto.getOpenIdCode());
            openId = authorizeInfo.getOpenId();
            unionId = authorizeInfo.getUnionId();
        } else {
            throw new BusinessException(ErrorCode.UNSUPPORTED_METHOD, "平台标志异常,仅可为1或2");
        }
        log.info("openid:{} unionId:{}",openId,unionId);
        ThrowUtils.throwIf(StringUtils.isEmpty(openId) && StringUtils.isEmpty(unionId), ErrorCode.WX_OPENID_INTERFACE_ERROR, "获取OpenID失败");
        if (StringUtils.isEmpty(unionId)) unionId=openId;
        User personnel = null;
        boolean userExistFlag = userthirdauthorizationService
                .lambdaQuery()
                .eq(Userthirdauthorization::getThirdSecondTag, openId).exists();
        // 用户存在 进行登录
        if (userExistFlag) {
            // 以前注册过的用户ID
            personnel = userService.getById(userthirdauthorizationService
                    .lambdaQuery()
                    .eq(Userthirdauthorization::getThirdFirstTag, unionId).list().get(0).getPersonnelId());
            ThrowUtils.throwIf(ObjectUtils.isEmpty(personnel), ErrorCode.NOT_FOUND_ERROR, "无法根据微信UNION_ID查询到该用户信息");
            // 新的OPENID不存在,需要插入
            if (!userthirdauthorizationService
                    .lambdaQuery()
                    .eq(Userthirdauthorization::getThirdFirstTag, unionId)
                    .eq(Userthirdauthorization::getThirdSecondTag, openId).exists()) {
                Userthirdauthorization userthirdauthorization = new Userthirdauthorization(personnel.getUserId(),
                        platform, ThirdAuthorizationType.Wechat.getVal(), unionId, openId);
                ThrowUtils.throwIf(!userthirdauthorizationService.save(userthirdauthorization),
                        ErrorCode.INSERT_ERROR, "插入用户第三方授权信息失败");
            }
        }
        // 用户不存在进行注册
        else {
            // 用户基本信息注册 以及各个三大实体的关联信息
            String nickname = nicknamerepoService.getRandomNick();
            personnel = new User(ids.nextId(), UUID.randomUUID().toString(), nickname);
            ThrowUtils.throwIf(!userService.save(personnel), ErrorCode.INSERT_ERROR, "插入新用户信息失败");
            // 存储第三方授权信息
            Userthirdauthorization userthirdauthorization = new Userthirdauthorization(personnel.getUserId(),
                    platform, ThirdAuthorizationType.Wechat.getVal(), unionId, openId);
            ThrowUtils.throwIf(!userthirdauthorizationService.save(userthirdauthorization),
                    ErrorCode.INSERT_ERROR, "插入用户第三方授权信息失败");
        }
        return new LoginStrategyRes(personnel.getUserId(),personnel.getRole(),personnel.getPhone(),!userExistFlag);
    }

    @Override
    public String getType() {
        return LoginTypeEnum.WX.getVal();
    }
}