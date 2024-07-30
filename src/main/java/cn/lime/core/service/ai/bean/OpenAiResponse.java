package cn.lime.core.service.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: OpenAiResponse
 * @Description: TODO
 * @Author: Lime
 * @Date: 2023/10/19 14:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiResponse implements Serializable {

    private String id;

    private String object;

    private Long created;

    private String model;

    private OpenAiUsageResponse usage;

    private List<OpenAiChoicesResponse> choices;
}
