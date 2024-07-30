package cn.lime.core.module.dto.unidto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: PhoneEasyLoginDto
 * @Description: 手机号一键登录
 * @Author: Lime
 * @Date: 2023/12/11 12:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PhoneEasyLoginDto extends UniEasyLoginDto implements Serializable {
    @Schema(description = "手机号")
    @NotNull
    private String phone;
    @Schema(description = "手机验证码")
    @NotNull
    private String code;
    @Schema(description = "邀请用户ID")
    private String inviteUserCode;
}
