package cn.lime.core.service.ai.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OpenAiUsageResponse
 * @Description: TODO
 * @Author: Lime
 * @Date: 2023/10/19 14:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiUsageResponse implements Serializable {

    @JSONField(name = "prompt_tokens")
    @JsonProperty(value = "prompt_tokens")
    private Integer promptTokens;
    @JSONField(name = "completion_tokens")
    @JsonProperty(value = "completion_tokens")
    private Integer completionTokens;
    @JSONField(name = "total_tokens")
    @JsonProperty(value = "total_tokens")
    private Integer totalTokens;

}
