package cn.lime.core.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long personnelId;

    private String token;

    private String accessToken;

    private String refreshToken;

    private Boolean isNew = false;

    private Boolean isBindPhone = false;

    private Integer accessTokenExpire;
    private Integer refreshTokenExpire;

    public LoginVo(Long personnelId, String token,Long tokenTtl) {
        this.personnelId = personnelId;
        this.token = token;
        this.accessTokenExpire = Math.toIntExact(tokenTtl);
    }

    public LoginVo(Long personnelId, String token, String refreshToken, Long accessTokenExpire, Long refreshTokenExpire) {
        this.personnelId = personnelId;
        this.token = token;
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
