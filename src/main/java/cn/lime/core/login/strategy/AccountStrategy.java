package cn.lime.core._login.strategy;

import cn.lime.core._aes.AesUtils;
import cn.lime.core._common.BusinessException;
import cn.lime.core._common.ErrorCode;
import cn.lime.core._common.ThrowUtils;
import cn.lime.core.module.dto.unidto.AccountLoginDto;
import cn.lime.core.module.dto.unidto.UniEasyLoginDto;
import cn.lime.core.module.entity.User;
import cn.lime.core.service.db.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AccountStrategy
 * @Description: 账号密码登录
 * @Author: riang
 * @Date: 2026/4/20 16:44
 */
@Service
@Slf4j
public class AccountStrategy implements LoginStrategy{
    @Resource
    private UserService userService;
    @Resource
    private AesUtils aesUtils;

    @Override
    public LoginStrategyRes loginCheck(UniEasyLoginDto baseDto) {
        AccountLoginDto dto = (AccountLoginDto) baseDto;
        User user = userService.lambdaQuery().eq(User::getAccount, dto.getAccount()).one();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(user), ErrorCode.NOT_FOUND_ERROR, "该账号不存在");
        try {
            String decode = aesUtils.decrypt(dto.getPwd());
            ThrowUtils.throwIf(!user.getPassword().equals(decode), ErrorCode.PWD_ERROR, "用户密码错误");
        }catch (Exception e){
            throw new BusinessException(ErrorCode.IO_ERROR,"解密用户密码失败");
        }
        return new LoginStrategyRes(user.getUserId(),user.getRole(),user.getPhone(),false);
    }

    @Override
    public String getType() {
        return LoginTypeEnum.ACCOUNT.getVal();
    }
}