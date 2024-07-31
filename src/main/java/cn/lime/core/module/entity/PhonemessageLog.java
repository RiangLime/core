package cn.lime.core.module.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

/**
 * 手机号短信记录表
 * @TableName PhoneMessage_Log
 */
@Data
@TableName(value ="PhoneMessage_Log")
public class PhonemessageLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 外部响应内容
     */
    @TableField(value = "outer_message")
    private String outerMessage;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    private static final long serialVersionUID = 1L;

    public PhonemessageLog(String phone, String outerMessage) {
        this.phone = phone;
        this.outerMessage = outerMessage;
    }
}