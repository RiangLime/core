package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: UserUpdatePwdByPhoneDto
 * @Description: 用户通过密码更新手机号
 * @Author: Lime
 * @Date: 2023/11/1 10:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePwdByPhoneDto implements Serializable {

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "手机验证码")
    private String code;

    @Schema(description = "新密码")
    private String newPwd;

}
