package cn.lime.core.annotation;


import cn.lime.core.constant.AuthLevel;
import cn.lime.core.constant.VipLevel;
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



}
