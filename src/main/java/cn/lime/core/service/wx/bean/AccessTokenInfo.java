package cn.lime.core.service.wx.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: AccessTokenInfo
 * @Description: 微信ACCESS token实体
 * @Author: Lime
 * @Date: 2023/10/17 16:28
 */
@Data
@NoArgsConstructor
public class AccessTokenInfo {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "expire_in")
    private Integer expireIn;


}
