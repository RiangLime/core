package cn.lime.core._login.token;

import cn.lime.core._config.CoreParams;
import cn.lime.core._login.config.TokenProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: AccessTokenHandlerLoader
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/30 12:53
 */
@Configuration
public class AccessTokenHandlerLoader {
    @Resource
    private TokenProperties params;

    @Bean
    public AccessTokenHandler accessTokenHandler(){
        return new AccessTokenHandler(params.getAccessTokenEncodeKey(),params.getAccessTokenExpires(),params.getRefreshTokenExpireHours());
    }
}
