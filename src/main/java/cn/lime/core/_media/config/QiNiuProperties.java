package cn.lime.core._media.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: OssProperties
 * @Description: oss配置信息
 * @Author: riang
 * @Date: 2026/4/20 17:45
 */
@Data
@Component
public class QiNiuProperties {
    // oss
    @Value("${core.oss.qiniu.token-expire:10}")
    private String qiNiuOssTokenExpire;
    @Value("${core.oss.qiniu.access-id:}")
    private String qiNiuOssAppId;
    @Value("${core.oss.qiniu.secret-id:}")
    private String qiNiuOssSecretId;
}