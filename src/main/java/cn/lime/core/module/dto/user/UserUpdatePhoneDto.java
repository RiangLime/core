package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserUpdatePhoneDto
 * @Description: 用户更新手机号
 * @Author: Lime
 * @Date: 2023/10/16 15:12
 */
@Data
public class UserUpdatePhoneDto implements Serializable {

    /**
     * 原手机号
     */
    @Schema(description = "旧手机号")
    @NotNull
    private String oldPhone;

    /**
     * 新手机号
     */
    @Schema(description = "新手机号")
    @NotNull
    private String newPhone;

    /**
     * 验证码
     */
    @Schema(description = "新手机号获得的验证码")
    @NotNull
    private String code;

}
