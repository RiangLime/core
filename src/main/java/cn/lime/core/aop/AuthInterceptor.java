package cn.lime.core.aop;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.module.vo.TokenCheckVo;
import cn.lime.core.service.login.UniLogService;
import cn.lime.core.threadlocal.ReqInfo;
import cn.lime.core.threadlocal.ReqThreadLocal;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * @ClassName: AuthInterceptor
 * @Description: 登录验证拦截器
 * @Author: Lime
 * @Date: 2023/6/26 18:53
 */
@Aspect
@Component
@Slf4j
@Order(3)
public class AuthInterceptor {

    public static final String TOKEN_HEADER = "Authorization";
    private static final String PLATFORM_HEADER = "Platform";

    @Resource
    private UniLogService uniLogService;


    /**
     * 登录/权限验证
     */
    @Before("@annotation(authCheck)")
    @Transactional
    public void doInterceptor(JoinPoint joinPoint, AuthCheck authCheck) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        ReqInfo userThreadLocalBean = ReqThreadLocal.getInfo();
        // 请求头校验
        checkHead(request, authCheck, userThreadLocalBean);
        // TOKEN校验
        if (authCheck.needToken()&& authCheck.authLevel().getVal() > AuthLevel.TOURIST.getVal()) {
            TokenCheckVo vo = uniLogService.checkJwtAccessToken(request.getHeader(TOKEN_HEADER));
            userThreadLocalBean.setUserId(vo.getUserId());
            userThreadLocalBean.setAuthLevel(vo.getAuthLevel());
            userThreadLocalBean.setVipLevel(vo.getVipLevel());
            ThrowUtils.throwIf(vo.getAuthLevel() < authCheck.authLevel().getVal(), ErrorCode.NO_AUTH_ERROR);
            ThrowUtils.throwIf(vo.getVipLevel() < authCheck.vipLevel().getVal(), ErrorCode.NO_AUTH_ERROR);

        }
        ReqThreadLocal.setInfo(userThreadLocalBean);

    }

    /**
     * 请求头校验
     *
     * @param request   请求体
     * @param authCheck 权限要求
     */
    private void checkHead(HttpServletRequest request, AuthCheck authCheck, ReqInfo userThreadLocalBean) {
        // 校验TOKEN头
        if (authCheck.needToken() && authCheck.authLevel().getVal() > AuthLevel.TOURIST.getVal()) {
            String token = request.getHeader(TOKEN_HEADER);
            ThrowUtils.throwIf(StringUtils.isEmpty(token), ErrorCode.PARAMS_ERROR, "该接口需携带token");
            userThreadLocalBean.setToken(token);
        }
        // 校验platform头
        if (authCheck.needPlatform()) {
            String platform = request.getHeader(PLATFORM_HEADER);
            ThrowUtils.throwIf(StringUtils.isEmpty(platform), ErrorCode.PARAMS_ERROR, "该接口需携带platform");
            userThreadLocalBean.setPlatform(Integer.valueOf(platform));
        }
    }
}
