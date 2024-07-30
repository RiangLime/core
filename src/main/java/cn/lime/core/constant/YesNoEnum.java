package cn.lime.core.constant;

/**
 * @ClassName: YesNoEnum
 * @Description: 真假枚举类型
 * @Author: Lime
 * @Date: 2023/10/17 18:07
 */
public enum YesNoEnum {

    YES(1),
    NO(0)

    ;
    private final int val;

    YesNoEnum(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }


}
