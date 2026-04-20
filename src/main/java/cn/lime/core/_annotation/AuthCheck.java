package cn.lime.core._annotation;


import cn.lime.core._constant.AuthLevel;
import cn.lime.core._constant.VipLevel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: AuthCheck
 * @Description: 权限校验
 * @Author: Lime
 * @Date: 2023/10/16 10:27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    VipLevel vipLevel() default VipLevel.NONE;

    AuthLevel authLevel() default AuthLevel.TOURIST;

    boolean needToken() default false;

    boolean needPlatform() default false;

    // 每个用户每一个api接口调用的最小间隔毫秒
    int rate() default 0;

}
