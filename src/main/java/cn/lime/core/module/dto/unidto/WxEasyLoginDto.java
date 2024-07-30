package cn.lime.core.module.dto.unidto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: WxEasyLoginDto
 * @Description: 微信一键登录
 * @Author: Lime
 * @Date: 2023/12/11 12:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WxEasyLoginDto extends UniEasyLoginDto implements Serializable {
    @Schema(description = "登录小程序端获取到的code")
    @NotNull
    private String openIdCode;
    @Schema(description = "邀请用户ID")
    private String inviteUserCode;
}
