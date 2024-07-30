package cn.lime.core.constant;

/**
 * @ClassName: NickWordTypeEnum
 * @Description: 昵称类型枚举类
 * @Author: Lime
 * @Date: 2023/12/4 14:24
 */
public enum NickWordTypeEnum {

    Noun(1),
    ADJ(2)

    ;
    private final Integer value;

    NickWordTypeEnum(Integer val){
        this.value = val;
    }

    public Integer getValue() {
        return value;
    }

}
