package cn.lime.core.service.ocr.ali;

import lombok.Data;

/**
 * @ClassName: AliOcrResponse
 * @Description: 阿里OCR请求响应
 * @Author: Lime
 * @Date: 2023/10/22 14:33
 */
@Data
public class AliOcrResponse {

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 数据字段
     */
    private String data;

    /**
     * 响应码
     */
    private String code;

    /**
     * 相应信息
     */
    private String message;

}
