package cn.lime.core._login.strategy;

import cn.lime.core._common.ErrorCode;
import cn.lime.core._common.ThrowUtils;
import cn.lime.core.module.dto.unidto.PhoneEasyLoginDto;
import cn.lime.core.module.dto.unidto.UniEasyLoginDto;
import cn.lime.core.module.entity.User;
import cn.lime.core.service.db.NicknamerepoService;
import cn.lime.core.service.db.UserService;
import cn.lime.core._sms.UniSmsService;
import cn.lime.core._snowflake.SnowFlakeGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName: PhoneStrategy
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/4/20 16:39
 */
@Service
@Slf4j
public class PhoneStrategy implements LoginStrategy{
    @Resource
    private UniSmsService smsService;
    @Resource
    private UserService userService;
    @Resource
    private NicknamerepoService nicknamerepoService;
    @Resource
    private SnowFlakeGenerator ids;


    @Override
    public LoginStrategyRes loginCheck(UniEasyLoginDto baseDto) {
        PhoneEasyLoginDto dto = (PhoneEasyLoginDto) baseDto;
        // 验证手机验证码
        ThrowUtils.throwIf(!smsService.checkCode(dto.getPhone(), dto.getCode()), ErrorCode.MOBILE_PHONE_MESSAGE_CODE_ERROR);
        User personnel = userService.lambdaQuery().eq(User::getPhone, dto.getPhone()).one();
        boolean userExistFlag = ObjectUtils.isNotEmpty(personnel);
        // 无该用户,需要进行注册
        if (!userExistFlag) {
            // 用户基本信息注册 以及各个三大实体的关联信息
            String nickname = nicknamerepoService.getRandomNick();
            personnel = new User(ids.nextId(), UUID.randomUUID().toString(), nickname);
            ThrowUtils.throwIf(!userService.save(personnel), ErrorCode.INSERT_ERROR, "插入新用户信息失败");
        }
        return new LoginStrategyRes(personnel.getUserId(),personnel.getRole(),personnel.getPhone(),!userExistFlag);
    }

    @Override
    public String getType() {
        return LoginTypeEnum.PHONE.getVal();
    }
}