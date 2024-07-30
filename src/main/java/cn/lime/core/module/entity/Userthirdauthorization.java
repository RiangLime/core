package cn.lime.core.module.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户第三方登录授权表
 * @author riang
 * @TableName UserThirdAuthorization
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="UserThirdAuthorization")
public class Userthirdauthorization implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 人员ID
     */
    @TableField(value = "personnel_id")
    private Long personnelId;

    /**
     * app平台类型
     */
    @TableField(value = "app_platform")
    private Integer appPlatform;

    /**
     * 第三方授权类型
     */
    @TableField(value = "third_type")
    private Integer thirdType;

    /**
     * 第三方平台一级标志
     */
    @TableField(value = "third_first_tag")
    private String thirdFirstTag;

    /**
     * 第三方平台二级标志 考虑到微信OPENID存在不同的情况
     */
    @TableField(value = "third_second_tag")
    private String thirdSecondTag;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private String gmtCreated;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    private String gmtModified;

    private static final long serialVersionUID = 1L;

    public Userthirdauthorization(Long personnelId, Integer appPlatform, Integer thirdType, String thirdFirstTag, String thirdSecondTag) {
        this.personnelId = personnelId;
        this.appPlatform = appPlatform;
        this.thirdType = thirdType;
        this.thirdFirstTag = thirdFirstTag;
        this.thirdSecondTag = thirdSecondTag;
    }
}