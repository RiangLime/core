package cn.lime.core.module.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 昵称工具表
 * @TableName NickNameRepo
 */
@Data
@TableName(value ="NickNameRepo")
public class Nicknamerepo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 单词类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 单词内容
     */
    @TableField(value = "content")
    private String content;

    private static final long serialVersionUID = 1L;
}