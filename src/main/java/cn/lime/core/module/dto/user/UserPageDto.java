package cn.lime.core.module.dto.user;

import cn.lime.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "模糊查询 可查昵称、账号、手机号")
    private String queryField;
    @Schema(description = "注册时间范围 开始 （秒级时间戳 ")
    private Long registerStart;
    @Schema(description = "注册时间范围 结束 （秒级时间戳")
    private Long registerEnd;
    @Schema(description = "用户状态 0被禁用 1正常")
    private Integer userState;
    @Schema(description = "用户 VIP等级")
    private Integer userVipLevel;
}
