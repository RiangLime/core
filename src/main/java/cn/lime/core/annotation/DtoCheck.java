package cn.lime.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: DtoCheck
 * @Description: DTO校验 1.IDtoCheck注释 2.Valid校验
 * @Author: Lime
 * @Date: 2023/9/26 16:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoCheck {

    boolean checkDto() default false;

    boolean checkBindResult() default false;

}
