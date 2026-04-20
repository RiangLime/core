package cn.lime.core.controller.admin;

import cn.lime.core._annotation.AuthCheck;
import cn.lime.core._annotation.DtoCheck;
import cn.lime.core._annotation.RequestLog;
import cn.lime.core._common.BaseResponse;
import cn.lime.core._common.PageResult;
import cn.lime.core._common.ResultUtils;
import cn.lime.core._constant.AuthLevel;
import cn.lime.core.module.dto.user.UserPageDto;
import cn.lime.core.module.vo.UserVo;
import cn.lime.core.service.db.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: AdminUserController
 * @Description: 管理员
 * @Author: Lime
 * @Date: 2024/7/31 12:15
 */
@RestController
@RequestMapping("/admin/user")
@Tag(name = "用户相关接口[管理员]")
@CrossOrigin(origins = "*")
@RequestLog
public class AdminUserController {

    @Resource
    private UserService userService;

    @PostMapping("/page")
    @Operation(summary = "用户分页查询")
    @AuthCheck(needToken = true,needPlatform = true,authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<PageResult<UserVo>> page(@Valid @RequestBody UserPageDto dto, BindingResult result) {
        return ResultUtils.success(userService.page(dto.getQueryField(), dto.getRegisterStart(), dto.getRegisterEnd(),
                dto.getUserState(), dto.getUserVipLevel(), dto.getCurrent(), dto.getPageSize(), dto.getSortOrder(),
                dto.getRealSortField()));
    }
}
