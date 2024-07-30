package cn.lime.core.constant;

/**
 * @ClassName: RedisKeyName
 * @Description: redis存储键名枚举
 * @Author: Lime
 * @Date: 2023/9/28 11:54
 */
public enum RedisKeyName {

    PAY_EXPIRE("order_pay")
    ;

    private final String val;

    RedisKeyName(String dbVal) {
        this.val = dbVal;
    }

    public String getVal() {
        return val;
    }

}
