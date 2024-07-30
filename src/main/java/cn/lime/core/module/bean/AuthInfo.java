package cn.lime.core.module.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: AuthInfo
 * @Description: 授权信息bean
 * @Author: Lime
 * @Date: 2024/2/19 10:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthInfo implements Serializable {
    /**
     * 机构ID
     */
    private Long institutionId;


    /**
     * 用户ID
     */private Long personnelId;


    /**
     * APP ID
     */
    private Long appId;

    /**
     * 用户是否为内部用户 0普通话用户 1内部用户
     */
    private Integer userType;

    /**
     * 用户是否正常使用
     */
    private Integer state;
}
