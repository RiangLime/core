package cn.lime.core.aop;

import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.common.dto.BaseCheckDto;
import cn.lime.core.common.dto.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

/**
 * @ClassName: DtoCheckInterceptor
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/31 10:49
 */
@Aspect
@Component
@Slf4j
@Order(2)
public class DtoCheckInterceptor {
    @Before("@annotation(dtoCheck)")
    @Transactional
    public void doInterceptor(JoinPoint joinPoint, DtoCheck dtoCheck) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) ThrowUtils.checkRequestValid((BindingResult) arg);
            if (arg instanceof PageRequest) ((PageRequest)arg).checkPageRequest();
            if (arg instanceof BaseCheckDto) ((BaseCheckDto) arg).checkRequest();
        }
    }
}
