package cn.lime.core.constant;

/**
 * @ClassName: PlatformEnum
 * @Description: 平台类型枚举类 1小程序 2\(^o^)/~
 * @Author: Lime
 * @Date: 2023/12/1 16:33
 */
public enum PlatformEnum {

    MP(1),
    H5(2)

    ;
    private final int val;

    PlatformEnum(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }

}
