package cn.lime.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLimit {
    // 每个用户每一个api接口调用的最小间隔毫秒
    int rate();
    // 判断有没有Token 如果有token就以userId-url判定，否则就以IP-URL判定
    boolean hasToken();
}
