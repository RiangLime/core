package cn.lime.core.loader;


import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ApplicationContextHolder
 * @Description: 全局容器
 * @Author: Lime
 * @Date: 2023/7/12 14:14
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) throws BeansException {
        ApplicationContextHolder.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

}
