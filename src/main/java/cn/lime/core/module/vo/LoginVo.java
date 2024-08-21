package cn.lime.core.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @ClassName: LoginVo
 * @Description: 登录vo
 * @Author: Lime
 * @Date: 2023/10/12 10:56
 */
@Data
@NoArgsConstructor
public class LoginVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "accessToken")
    private String accessToken;
    @Schema(description = "refreshToken")
    private String refreshToken;
    @Schema(description = "是否为新注册用户")
    private Boolean isNew = false;
    @Schema(description = "是否绑定了手机号")
    private Boolean isBindPhone = false;
    @Schema(description = "Token剩余过期时间 单位秒")
    private Integer accessTokenExpire;
    @Schema(description = "Token剩余过期时间 单位秒")
    private Integer refreshTokenExpire;

    public LoginVo(Long personnelId, String token, String refreshToken, Long accessTokenExpire, Long refreshTokenExpire) {
        this.userId = personnelId;
        this.accessToken = token;
        this.refreshToken = refreshToken;
        if (ObjectUtils.isNotEmpty(accessTokenExpire)) {
            this.accessTokenExpire = Math.toIntExact(accessTokenExpire);
        }
        if (ObjectUtils.isNotEmpty(refreshTokenExpire)) {
            this.refreshTokenExpire = Math.toIntExact(refreshTokenExpire);
        }
    }
}
