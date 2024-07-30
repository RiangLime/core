package cn.lime.core.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: TokenCheckVo
 * @Description: token校验vo
 * @Author: Lime
 * @Date: 2023/10/17 14:35
 */
@Data
@NoArgsConstructor
public class TokenCheckVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 用户权限等级
     * 0 none游客
     * 1 inner已登录用户
     * 2 admin管理员
     */
    private Integer authLevel;

    /**
     * VIP等级
     */
    private Integer vipLevel;
}