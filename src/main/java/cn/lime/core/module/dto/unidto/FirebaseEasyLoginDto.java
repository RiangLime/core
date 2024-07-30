package cn.lime.core.module.dto.unidto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: FirebaseEasyLoginDto
 * @Description: firebase一键登录
 * @Author: Lime
 * @Date: 2023/12/11 12:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseEasyLoginDto extends UniEasyLoginDto implements Serializable {
    @Schema(description = "第三方登录类型 1手机号 2邮箱登录 3Google 4Facebook 5Github 6Microsoft 7Twitter")
    @NotNull
    private Integer thirdLoginType;
    @Schema(description = "uid")
    @NotNull
    private String uid;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "邀请用户ID")
    private String inviteUserCode;
}
