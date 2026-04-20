package cn.lime.core._sms.strategy;

/**
 *
 */
public enum SmsTypeEnum {
    ALI("ali")
    ;
    private final String val;

    public String getVal() {
        return val;
    }

    SmsTypeEnum(String val) {
        this.val = val;
    }
}
