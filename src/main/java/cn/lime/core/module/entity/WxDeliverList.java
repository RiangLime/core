package cn.lime.core.module.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信运力表
 * @TableName Wx_Deliver_List
 */
@TableName(value ="Wx_Deliver_List")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxDeliverList implements Serializable {
    /**
     * 公司ID
     */
    @TableField(value = "delivery_id")
    private String deliveryId;

    /**
     * 公司名
     */
    @TableField(value = "delivery_name")
    private String deliveryName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}