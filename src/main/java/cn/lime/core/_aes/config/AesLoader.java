package cn.lime.core._aes.config;

import cn.lime.core._aes.AesUtils;
import cn.lime.core._config.CoreParams;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: AesLoader
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/4/7 18:14
 */
@Configuration
public class AesLoader {

    @Resource
    private AesProperties params;

    @Bean
    public AesUtils aesUtils(){
        return new AesUtils(params.getKey(),params.getIv());
    }

}
