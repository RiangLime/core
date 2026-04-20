package cn.lime.core._snowflake.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SnowflakeProperties
 * @Description: 雪花ID配置
 * @Author: riang
 * @Date: 2026/4/20 17:39
 */
@Data
@Component
public class SnowflakeProperties {
    // snowFlake
    @Value("${core.snowflake.datacenter-id:1}")
    private long datacenterId;
    @Value("${core.snowflake.machine-id:1}")
    private long machineId;
}