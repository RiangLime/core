package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserBindPhoneDto
 * @Description: 用户绑定手机号DTO
 * @Author: Lime
 * @Date: 2023/10/18 14:05
 */
@Data
public class UserBindPhoneDto implements Serializable {

    @Schema(description = "小程序获取到的手机对应的码")
    @NotNull
    private String phoneCode;

}
