package cn.lime.core.service.ocr.ali;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AliOcrRequest
 * @Description: 阿里OCR请求
 * @Author: Lime
 * @Date: 2023/10/22 14:32
 */
@Data
public class AliOcrRequest implements Serializable {

    /**
     * URL
     */
    private String url;

    /**
     * 内容
     */
    private String body;

    public AliOcrRequest(String url, String body) {
        this.url = url;
        this.body = body;
    }
}
