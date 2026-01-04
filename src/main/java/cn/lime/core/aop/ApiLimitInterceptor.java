package cn.lime.core.aop;

import cn.lime.core.annotation.ApiLimit;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.threadlocal.ReqThreadLocal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ApiLimitInterceptor
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/1/4 21:12
 */
@Aspect
@Component
@Slf4j
@Order(4)
public class ApiLimitInterceptor {

    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;


    @Before("@annotation(apiLimit)")
    @Transactional
    public void doInterceptor(JoinPoint joinPoint, ApiLimit apiLimit) {
        StringBuilder redisKey = new StringBuilder();
        if (apiLimit.hasToken()) {
            redisKey.append(ReqThreadLocal.getInfo().getUserId());
        } else {
            redisKey.append(ReqThreadLocal.getInfo().getIp());
        }
        redisKey.append("-");
        redisKey.append(ReqThreadLocal.getInfo().getUri());
        Boolean res = redisTemplateMap.get(RedisDb.API_LIMIT.getVal()).opsForValue()
                .setIfAbsent(redisKey.toString(), "1", apiLimit.rate(), TimeUnit.MILLISECONDS);
        ThrowUtils.throwIf(Boolean.FALSE.equals(res), ErrorCode.API_LIMIT_EXCEED);
    }

}