package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UserRegisterDto
 * @Description: 用户注册DTO
 * @Author: Lime
 * @Date: 2023/10/31 17:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto implements Serializable {

    @Schema(description = "账号名")
    @NotNull
    private String account;
    @Schema(description = "密码")
    @NotNull
    private String password;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "手机号")
    @NotNull
    private String phone;
    @Schema(description = "手机号验证码")
    @NotNull
    private String code;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "性别")
    private Integer sex;
    @Schema(description = "生日")
    private Date birthday;
    @Schema(description = "籍贯")
    private String birthplace;

}
