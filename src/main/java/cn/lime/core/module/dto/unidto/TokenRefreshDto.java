package cn.lime.core.module.dto.unidto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: TokenRefreshDto
 * @Description: 刷新token
 * @Author: Lime
 * @Date: 2023/12/13 11:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshDto implements Serializable {

    @NotNull
    private String refreshToken;


}
