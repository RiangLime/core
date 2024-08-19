package cn.lime.core.aop;


import cn.lime.core.threadlocal.ReqInfo;
import cn.lime.core.threadlocal.ReqThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * @ClassName: LogInterceptor
 * @Description: 日志
 * @Author: Lime
 * @Date: 2023/11/8 15:41
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LogInterceptor {


    /**
     * 切点
     */
    @Pointcut("within(@cn.lime.core.annotation.RequestLog *)")
    public void webLog() {
    }

    /**
     * Log打印
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //在controller层以外获取当前正在处理的请求，固定格式
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String url = request.getRequestURI();

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        String params = null;

        ReqInfo bean = new ReqInfo();
        bean.setUuid(UUID.randomUUID().toString());
        bean.setIp(ip);
        bean.setReqTime(System.currentTimeMillis());
        ReqThreadLocal.setInfo(bean);

        if ("POST".equals(method) || "PUT".equals(method)) {
            //joinPoint 就是targer
            //获取target方法中的所有参数
            //如果target = sendCode 那么args = params
            //如果target = register 那么args = userParam
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                params = args[0].toString();
            }
        }
        //打印日志
        log.info("ReqId:[{}] IP[{}]\tMETHOD[{}]\tURL[{}]\tPARAMS[{}]",
                ReqThreadLocal.getInfo().getUuid(), ip, className + "." + methodName, url, params);


    }

    /**
     * Log打印
     */
    @After("webLog()")
    public void afterTime() {
        long betweenTime = System.currentTimeMillis() - ReqThreadLocal.getInfo().getReqTime();
        log.info("ReqId:[{}] 耗时：{}ms", ReqThreadLocal.getInfo().getUuid(), betweenTime);
        ReqThreadLocal.remove();
    }

}
