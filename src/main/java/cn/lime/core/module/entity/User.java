package cn.lime.core.module.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="User")
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 账号
     */
    @TableField(value = "account")
    private String account;

    /**
     * 密码
     */
    @TableField(value = "")
    private String password;

    /**
     * 用户昵称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 手机号码
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 出生日期
     */
    @TableField(value = "birthday")
    private Date birthday;

    /**
     * 籍贯
     */
    @TableField(value = "birthplace")
    private String birthplace;

    /**
     * 用户状态 0被禁用 1正常
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(value = "role")
    private Integer role;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public User(Long userId, String account, String name) {
        this.userId = userId;
        this.account = account;
        this.name = name;
    }

    public User(Long userId, String account, String password, String name, String avatar, String phone, String email,
                Integer sex, Date birthday, String birthplace) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;
        this.birthplace = birthplace;
    }
}