package cn.lime.core._config.resttemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName: RestTemplateConfig
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/2/26 23:56
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 初始化RestTemplate Bean，解决UTF-8编码问题，配置基础超时
     * @return 配置好的RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);

        // 核心：替换String消息转换器为UTF-8编码（解决响应乱码）
        // 1. 移除默认的StringHttpMessageConverter（默认ISO-8859-1编码）
        restTemplate.getMessageConverters().removeIf(
                converter -> converter instanceof StringHttpMessageConverter
        );
        // 2. 添加UTF-8版本的String转换器（放到最前面，优先使用）
        restTemplate.getMessageConverters().add(0,
                new StringHttpMessageConverter(StandardCharsets.UTF_8)
        );

        return restTemplate;
    }

    /**
     * 配置请求工厂：设置连接超时、读取超时
     * @return ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时：5秒（单位：毫秒），超过时间未建立连接则抛异常
        factory.setConnectTimeout(5000);
        // 读取超时：10秒（单位：毫秒），超过时间未读取到响应则抛异常
        factory.setReadTimeout(10000);
        return factory;
    }
}