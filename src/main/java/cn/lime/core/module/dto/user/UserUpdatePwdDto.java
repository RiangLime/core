package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserUpdatePwdDto
 * @Description: 用户更新密码
 * @Author: Lime
 * @Date: 2023/10/16 15:15
 */
@Data
public class UserUpdatePwdDto implements Serializable {

    @Schema(description = "旧密码")
    @NotNull
    private String pwd;

    @Schema(description = "新密码")
    @NotNull
    private String newPwd;

}
