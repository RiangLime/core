package cn.lime.core.constant;

/**
 * @ClassName: AuthLevel
 * @Description: 授权等级 0无权限用户 1内部用户 2管理员用户
 * @Author: Lime
 * @Date: 2023/12/28 10:59
 */
public enum AuthLevel {


    TOURIST(0),
    USER(1),
    ADMIN(2),

    ;
    private final int val;

    AuthLevel(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }

}
