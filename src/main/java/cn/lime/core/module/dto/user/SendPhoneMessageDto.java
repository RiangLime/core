package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SendPhoneMessageDto
 * @Description: 发送手机验证码
 * @Author: Lime
 * @Date: 2023/10/25 09:59
 */
@Data
public class SendPhoneMessageDto implements Serializable {

    @Schema(description = "mobilephone")
    private String mobile;

}
