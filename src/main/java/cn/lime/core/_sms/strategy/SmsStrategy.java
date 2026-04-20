package cn.lime.core._sms.strategy;

import cn.lime.core._common.BusinessCatchableException;

/**
 * @ClassName: SmsStrategy
 * @Description: sms策略类
 * @Author: riang
 * @Date: 2026/4/20 18:06
 */
public interface SmsStrategy {
    String getType();
    void sendMobilePhone(String mobilePhone, String code)throws Exception;
}