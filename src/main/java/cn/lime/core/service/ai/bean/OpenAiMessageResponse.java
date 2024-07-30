package cn.lime.core.service.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OpenAiMessageResponse
 * @Description: TODO
 * @Author: Lime
 * @Date: 2023/10/19 14:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiMessageResponse implements Serializable {

    private String role;
    private String content;

}
