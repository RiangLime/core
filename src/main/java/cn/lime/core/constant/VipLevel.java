package cn.lime.core.constant;

/**
 * @ClassName: VipLevel
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/30 11:34
 */
public enum VipLevel {
    NONE(0),
    ONE(1),
    TWO(2),

    ;
    private final int val;

    VipLevel(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }
}
