package cn.lime.core.loader;

import cn.lime.core.aes.AesUtils;
import cn.lime.core.config.CoreParams;
import cn.lime.core.token.AccessTokenHandler;
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
    private CoreParams params;

    @Bean
    public AccessTokenHandler aesUtils(){
        return new AccessTokenHandler(params.getAccessTokenEncodeKey());
    }
}
