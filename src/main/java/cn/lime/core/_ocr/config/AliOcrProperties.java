package cn.lime.core._ocr.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: OcrProperties
 * @Description: OCR配置信息
 * @Author: riang
 * @Date: 2026/4/20 17:44
 */
@Data
@Component
public class AliOcrProperties {
    // ocr
    @Value("${core.ocr.ali.access-id:}")
    private String aliOcrAccessId;
    @Value("${core.ocr.ali.secret-id:}")
    private String aliOcrSecretId;
    @Value("${core.ocr.ali.endpoint:}")
    private String aliOcrEndPoint;
}