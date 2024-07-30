package cn.lime.core.service.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OpenAiMessageBean
 * @Description: TODO
 * @Author: Lime
 * @Date: 2023/12/20 14:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiMessageBean implements Serializable {
    private String role;
    private String content;
}
