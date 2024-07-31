package cn.lime.core.module.dto.user;

import cn.lime.core.common.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: UserPageDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/31 13:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto extends PageRequest implements Serializable {
    private String queryField;
    private Long registerStart;
    private Long registerEnd;
    private Integer userState;
    private Integer userVipLevel;
}
