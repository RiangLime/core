package cn.lime.core.service.ai.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OpenAiChoicesResponse
 * @Description: TODO
 * @Author: Lime
 * @Date: 2023/10/19 14:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiChoicesResponse implements Serializable {

    private Integer index;

    private OpenAiMessageResponse message;

    @JsonProperty(value = "finish_reason")
    @JSONField(name = "finish_reason")
    private String finishReason;

}
