package cn.lime.core.module.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

/**
 * 用户登录日志表
 * @TableName Login_Log
 */
@Data
@TableName(value ="Login_Log")
public class LoginLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 登录IP
     */
    @TableField(value = "login_ip")
    private String loginIp;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    private Long loginTime;

    /**
     * 登录平台 1微信小程序 2PC端 3安卓APP 4苹果APP 5iOS平板
     */
    @TableField(value = "login_platform")
    private Integer loginPlatform;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Long gmtCreated;

    private static final long serialVersionUID = 1L;
}