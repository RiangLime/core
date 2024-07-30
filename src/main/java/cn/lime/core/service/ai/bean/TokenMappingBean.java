package cn.lime.core.service.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: TokenMappingBean
 * @Description: 通过业务接口
 * @Author: Lime
 * @Date: 2023/12/21 17:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenMappingBean {
    private String token;
    private String url;
    private Long institutionId;
    private Long appId;
}
