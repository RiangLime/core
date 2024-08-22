package cn.lime.core.constant;

/**
 * @ClassName: RedisKeyName
 * @Description: redis存储键名枚举
 * @Author: Lime
 * @Date: 2023/9/28 11:54
 */
public enum RedisKeyName {

    PAY_EXPIRE("order_pay"),
    BIZ_NOTICE_SUCCESS_PAID_ORDER("paid_order_id_notice_queue"),
    ;

    private final String val;

    RedisKeyName(String dbVal) {
        this.val = dbVal;
    }

    public String getVal() {
        return val;
    }

}
