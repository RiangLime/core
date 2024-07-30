package cn.lime.core.module.dto.unidto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: AccountLoginDto
 * @Description: 账号密码登录
 * @Author: Lime
 * @Date: 2023/12/19 10:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginDto extends UniEasyLoginDto implements Serializable {

    @Schema(description = "账号")
    @NotNull
    private String account;
    @Schema(description = "密码")
    @NotNull
    private String pwd;

}
