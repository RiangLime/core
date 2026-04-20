package cn.lime.core._login.strategy;

import cn.lime.core._common.ErrorCode;
import cn.lime.core._common.ThrowUtils;
import cn.lime.core.module.dto.unidto.FirebaseEasyLoginDto;
import cn.lime.core.module.dto.unidto.UniEasyLoginDto;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.service.db.NicknamerepoService;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.db.UserthirdauthorizationService;
import cn.lime.core._snowflake.SnowFlakeGenerator;
import cn.lime.core._threadlocal.ReqThreadLocal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName: FirebaseStragety
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/4/20 16:51
 */
@Service
@Slf4j
public class FirebaseStrategy implements LoginStrategy{
    @Resource
    private UserthirdauthorizationService userthirdauthorizationService;
    @Resource
    private UserService userService;
    @Resource
    private NicknamerepoService nicknamerepoService;
    @Resource
    private SnowFlakeGenerator ids;

    @Override
    public LoginStrategyRes loginCheck(UniEasyLoginDto baseDto) {
        FirebaseEasyLoginDto dto = (FirebaseEasyLoginDto) baseDto;
        // 获取平台标志 1Mp 2H5 3...
        Integer platform = ReqThreadLocal.getInfo().getPlatform();
        Userthirdauthorization userthirdauthorization = userthirdauthorizationService
                .lambdaQuery()
                .eq(Userthirdauthorization::getThirdType, dto.getThirdLoginType())
                .eq(Userthirdauthorization::getThirdFirstTag, dto.getUid()).one();
        User personnel = ObjectUtils.isEmpty(userthirdauthorization) ? null :
                userService.getById(userthirdauthorization.getPersonnelId());
        boolean userExist = ObjectUtils.isEmpty(userthirdauthorization);
        // 如果为空 要进行注册
        if (!userExist) {
            String nickname = nicknamerepoService.getRandomNick();
            personnel = new User(ids.nextId(), UUID.randomUUID().toString(), nickname);
            ThrowUtils.throwIf(!userService.save(personnel), ErrorCode.INSERT_ERROR, "插入新用户信息失败");
            // 存储第三方授权信息
            Userthirdauthorization bean = new Userthirdauthorization(personnel.getUserId(),
                    platform, dto.getThirdLoginType(), dto.getUid(), null);
            ThrowUtils.throwIf(!userthirdauthorizationService.save(bean),
                    ErrorCode.INSERT_ERROR, "插入用户第三方授权信息失败");
        }
        return new LoginStrategyRes(personnel.getUserId(),personnel.getRole(),personnel.getPhone(),!userExist);
    }

    @Override
    public String getType() {
        return LoginTypeEnum.FIREBASE.getVal();
    }
}