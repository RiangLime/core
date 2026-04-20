package cn.lime.core._snowflake.config;

import cn.lime.core._config.CoreParams;
import cn.lime.core._snowflake.SnowFlakeGenerator;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: SnowFlakeGeneratorLoader
 * @Description: 雪花生成器初始化类
 * @Author: Lime
 * @Date: 2023/6/26 18:14
 */
@Configuration
public class SnowFlakeGeneratorLoader {

    @Resource
    private SnowflakeProperties param;

    @Bean
    public SnowFlakeGenerator loadSnowFlakeGenerator(){
        return new SnowFlakeGenerator(param.getDatacenterId(),param.getMachineId());
    }

}
