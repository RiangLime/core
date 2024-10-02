package cn.lime.core.constant;

/**
 * @ClassName: RedisDb
 * @Description: redis数据库存储枚举
 * @Author: Lime
 * @Date: 2023/9/18 17:10
 */
public enum RedisDb {

    TOKEN(1),
    VERIFICATION(2),
    PAYMENT_EXPIRE_DB(3),
    WX_ACCESS_TOKEN(5),
    BIZ_DB(10)
    ;

    private final int val;

    RedisDb(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }

}
