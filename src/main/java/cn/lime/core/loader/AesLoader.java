package cn.lime.core.loader;

import cn.lime.core.aes.AesUtils;
import cn.lime.core.config.CoreParams;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
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
    private CoreParams params;

    @Bean
    public AesUtils aesUtils(){
        return new AesUtils(params.getKey(),params.getIv());
    }

}
