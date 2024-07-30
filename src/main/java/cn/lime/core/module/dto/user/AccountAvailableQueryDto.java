package cn.lime.core.module.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AccountAvailableQueryDto
 * @Description: 查询账号是否可用
 * @Author: Lime
 * @Date: 2023/10/16 15:08
 */
@Data
public class AccountAvailableQueryDto implements Serializable {

    @Schema(description = "账号名")
    private String account;

}
