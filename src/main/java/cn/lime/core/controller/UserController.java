package cn.lime.core.controller;

import cn.lime.core.annotation.ApiLimit;
import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.module.dto.*;
import cn.lime.core.module.dto.unidto.*;
import cn.lime.core.module.dto.user.*;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.vo.LoginVo;
import cn.lime.core.module.vo.UserVo;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.login.UniLogService;
import cn.lime.core.service.phone.BasePhoneService;
import cn.lime.core.threadlocal.ReqThreadLocal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: UserController
 * @Description: 用户相关接口
 * @Author: Lime
 * @Date: 2023/10/16 14:12
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关接口")
@CrossOrigin(origins = "*")
@RequestLog
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private BasePhoneService basePhoneService;
    @Resource
    private UniLogService uniLogService;


    @PostMapping("/check/legal/account")
    @Operation(summary = "用户查询账号是否可用")
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = false, rate = 1000)
    public BaseResponse<Boolean> queryAccountLegal(@RequestBody@Valid AccountAvailableQueryDto registerDto, BindingResult result){
        return ResultUtils.success(!userService.lambdaQuery().eq(User::getAccount,registerDto.getAccount()).exists());
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册 不推荐使用")
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = false, rate = 1000)
    public BaseResponse<Void> register(@RequestBody@Valid UserRegisterDto dto, BindingResult result){
        userService.register(dto.getAccount(),dto.getPassword(),dto.getNickname(),dto.getPhone(),dto.getCode(),
                dto.getAvatar(),dto.getEmail(),dto.getSex(),dto.getBirthday(),dto.getBirthplace());
        return ResultUtils.success(null);
    }

    @PostMapping("/phonemessage")
    @Operation(summary = "手机号发送短信")
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = false, rate = 5000)
    public BaseResponse<Void> sendPhoneMessage(@Valid@RequestBody SendPhoneMessageDto dto, BindingResult result){
        basePhoneService.sendMessage(dto.getMobile());
        return ResultUtils.success(null);
    }

    @PostMapping("/easylogin/phone")
    @Operation(summary = "一键登录 手机号")
    @DtoCheck(checkBindResult = true)
    @AuthCheck(needPlatform = true)
    @ApiLimit(hasToken = false, rate = 1000)
    public BaseResponse<LoginVo> easyLoginPhone(@Valid @RequestBody PhoneEasyLoginDto request, BindingResult result){
        return ResultUtils.success(uniLogService.easyLogin(request));
    }

    @PostMapping("/easylogin/wx")
    @Operation(summary = "一键登录 微信")
    @DtoCheck(checkBindResult = true)
    @AuthCheck(needPlatform = true)
    @ApiLimit(hasToken = false, rate = 1000)
    public BaseResponse<LoginVo> easyLoginWx(@Valid @RequestBody WxEasyLoginDto request, BindingResult result){
        return ResultUtils.success(uniLogService.easyLogin(request));
    }


    @PostMapping("/easylogin/third")
    @Operation(summary = "一键登录 第三方")
    @DtoCheck(checkBindResult = true)
    @AuthCheck(needPlatform = true)
    @ApiLimit(hasToken = false, rate = 1000)
    public BaseResponse<LoginVo> easyLoginThird(@Valid@RequestBody FirebaseEasyLoginDto request, BindingResult result){
        return ResultUtils.success(uniLogService.easyLogin(request));
    }

    /**
     * 账号密码登录
     * @param request
     * @param result
     * @return
     */
    @PostMapping("/login/account")
    @Operation(summary = "账号密码登录")
    @AuthCheck(needPlatform = true)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = false, rate = 1000)
    public BaseResponse<LoginVo> loginAccount(@Valid @RequestBody AccountLoginDto request, BindingResult result) {
        return ResultUtils.success(uniLogService.easyLogin(request));
    }

    @PostMapping("/update/common")
    @Operation(summary = "用户更新信息 一般信息")
    @AuthCheck(needToken = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> updateUserCommon(@Valid @RequestBody UserUpdateCommonDto request, BindingResult result) {
        userService.updateCommonInfo(request.getAccount(),request.getNickName(),request.getAvatar(),
                request.getEmail(),request.getSex(),request.getBirthday(),request.getBirthplace());
        return ResultUtils.success(null);
    }

    @PostMapping("/update/pwd")
    @Operation(summary = "用户更新信息 密码")
    @AuthCheck(needToken = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> updateUserPwd(@Valid @RequestBody UserUpdatePwdDto request, BindingResult result) {
        userService.updatePwdInfo(request.getPwd(),request.getNewPwd());
        return ResultUtils.success(null);
    }

    @PostMapping("/update/pwd/phone")
    @Operation(summary = "用户更新信息 通过手机号修改密码")
    @AuthCheck(needToken = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> updateUserPwd(@Valid @RequestBody UserUpdatePwdByPhoneDto request, BindingResult result) {
        userService.updatePwdInfo(request.getMobile(),request.getCode(),request.getNewPwd());
        return ResultUtils.success(null);
    }

    @PostMapping("/update/mobile")
    @Operation(summary = "用户更新信息 手机号")
    @AuthCheck(needToken = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> updateUserMobile(@Valid @RequestBody UserUpdatePhoneDto request, BindingResult result) {
        userService.updatePhone(request.getOldPhone(),request.getNewPhone(),request.getCode());
        return ResultUtils.success(null);
    }

    @PostMapping("/update/mobile/first")
    @Operation(summary = "用户首次绑定手机号 返回信息如果有值就是新登录信息")
    @AuthCheck(needToken = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<LoginVo> bindUserMobile(@Valid @RequestBody UserBindPhoneDto request, BindingResult result) {
        return ResultUtils.success(userService.bindPhoneByPhoneCode(request.getPhoneCode()));
    }

    @PostMapping("/update/mobile/first/byphone")
    @Operation(summary = "用户首次绑定手机号 返回信息如果有值就是新登录信息")
    @AuthCheck(needToken = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<LoginVo> bindUserMobile(@Valid @RequestBody PhoneEasyLoginDto request, BindingResult result) {
        return ResultUtils.success(userService.bindPhoneByPhone(request.getPhone(),request.getCode()));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    @AuthCheck(needToken = true,needPlatform = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<Void> logout(@Valid @RequestBody EmptyDto request, BindingResult result) {
        uniLogService.logout();
        return ResultUtils.success(null);
    }

    @PostMapping("/detail")
    @Operation(summary = "用户详情")
    @AuthCheck(needToken = true,needPlatform = true,authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    @ApiLimit(hasToken = true, rate = 1000)
    public BaseResponse<UserVo> detail(@Valid @RequestBody EmptyDto request, BindingResult result) {
        return ResultUtils.success(userService.detail(ReqThreadLocal.getInfo().getUserId()));
    }


    @PostMapping("/token/refresh")
    @Operation(summary = "刷新accessToken")
    @DtoCheck(checkBindResult = true)
    @AuthCheck()
    public BaseResponse<String> refreshToken(@Valid @RequestBody TokenRefreshDto dto, BindingResult result){
        return ResultUtils.success(uniLogService.refreshToken(dto.getRefreshToken()));
    }


}
