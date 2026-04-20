package cn.lime.core._login.strategy;

/**
 * 登录类型
 */
public enum LoginTypeEnum {
    ACCOUNT("account"),
    PHONE("phone"),
    FIREBASE("firebase"),
    WX("wx")
    ;
    private final String val;

    public String getVal() {
        return val;
    }

    LoginTypeEnum(String val) {
        this.val = val;
    }
}
