package cn.lime.core.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author riang
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String ERROR_STRING_INFO = "<![CDATA[REQ_ERROR]]>";

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException [{}]", e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException,[{}]", e.getMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "解析DTO失败，请检查DTO是否合法 / 接口URL是否正确");
    }


}