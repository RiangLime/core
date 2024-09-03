package cn.lime.core.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 本地多媒体文件表
 * @TableName Local_Media
 */
@TableName(value ="Local_Media")
@Data
public class LocalMedia implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 多媒体URL
     */
    @TableField(value = "url")
    private String url;

    /**
     * 多媒体标签
     */
    @TableField(value = "url_tag_id")
    private Long urlTagId;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}