package cn.lime.core._ai.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: AiProperties
 * @Description: ai配置 对接openai
 * @Author: riang
 * @Date: 2026/4/20 17:41
 */
@Data
@Component
public class AiProperties {
    // ai
    @Value("${core.openai.url:}")
    private String aiUrl;
    @Value("${core.openai.api-key:}")
    private String aiKey;
}