package cn.lime.core._login.strategy;

import lombok.Data;

/**
 * @ClassName: LoginStrategyRes
 * @Description: 登录策略结果
 * @Author: riang
 * @Date: 2026/4/20 16:34
 */
@Data
public class LoginStrategyRes {
    private long userId;
    private int userRole;
    private String mobile;
    private boolean isNewUser;

    public LoginStrategyRes(long userId, String mobile, boolean isNewUser) {
        this.userId = userId;
        this.mobile = mobile;
        this.isNewUser = isNewUser;
    }

    public LoginStrategyRes(long userId, int userRole, String mobile, boolean isNewUser) {
        this.userId = userId;
        this.userRole = userRole;
        this.mobile = mobile;
        this.isNewUser = isNewUser;
    }
}