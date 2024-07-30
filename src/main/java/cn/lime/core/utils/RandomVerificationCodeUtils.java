package cn.lime.core.utils;

/**
 * @ClassName: RandomVerificationCodeUtils
 * @Description: 生成随机验证码
 * @Author: Lime
 * @Date: 2023/6/29 17:31
 */
public class RandomVerificationCodeUtils {

    /**
     * 生成六位随机验证码
     *
     * @return 六位数长度的验证码
     */
    public static String nextCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
    }

}
