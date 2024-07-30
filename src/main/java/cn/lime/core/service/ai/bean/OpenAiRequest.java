package cn.lime.core.service.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: OpenAiOuterRequest
 * @Description: 外部统一访问AI请求体
 * @Author: Lime
 * @Date: 2023/12/20 14:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiRequest implements Serializable {

    private static final String DEFAULT_MODEL = "gpt-3.5-turbo";
    private static final Boolean DEFAULT_STREAM = false;
    private static final Double DEFAULT_TEMPERATURE = 0.7;

    /**
     * 必填参数
     */
    private String model = DEFAULT_MODEL;
    private Boolean stream = DEFAULT_STREAM;
    private Double temperature = DEFAULT_TEMPERATURE;
    private List<OpenAiMessageBean> messages;

    /*
     * 选填参数
     */


}
