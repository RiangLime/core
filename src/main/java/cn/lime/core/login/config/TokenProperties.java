package cn.lime.core._login.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TokenProperties
 * @Description: token相关配置
 * @Author: riang
 * @Date: 2026/4/20 17:37
 */
@Data
@Component
public class TokenProperties {
    @Value("${core.token.access-expire-millis:3600000}")
    private Long accessTokenExpires;
    @Value("${core.token.refresh-expire-hours:720}")
    private Integer refreshTokenExpireHours;
    // token key
    @Value("${core.token.aes-key:ibviuziu18g48b1}")
    private String accessTokenEncodeKey;
}