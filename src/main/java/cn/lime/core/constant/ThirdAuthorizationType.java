package cn.lime.core.constant;

/**
 * @ClassName: ThirdAuthorizationType
 * @Description: 1手机号 2邮箱登录 3Google 4Facebook 5Github 6Microsoft 7Twitter 99微信
 * @Author: Lime
 * @Date: 2023/12/11 13:48
 */
public enum ThirdAuthorizationType {

    PHONE(1),
    MAIL(2),
    Google(3),
    Facebook(4),
    Github(5),
    Microsoft(6),
    Twitter(7),
    Wechat(99),

    ;
    private final int val;

    ThirdAuthorizationType(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }

}
