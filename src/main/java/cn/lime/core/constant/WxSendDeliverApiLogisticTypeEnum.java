package cn.lime.core.constant;

/**
 * @ClassName: WxSendDeliverApiLogisticTypeEnum
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/2/18 00:17
 */
public enum WxSendDeliverApiLogisticTypeEnum {

    DELIVER(1),
    SAME_CITY(2),
    ABSTRACT(3),
    SELF_GET(4)

    ;

    private final int val;

    WxSendDeliverApiLogisticTypeEnum(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }

}