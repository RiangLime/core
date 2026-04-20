package cn.lime.core._aes.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: AesProperties
 * @Description: aes配置信息
 * @Author: riang
 * @Date: 2026/4/20 17:40
 */
@Data
@Component
public class AesProperties {
    // aes
    @Value("${core.aes.key:1234123412341234}")
    private String key;
    @Value("${core.aes.iv:1234123412341234}")
    private String iv;
}