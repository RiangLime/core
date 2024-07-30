package cn.lime.core.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static java.util.Objects.isNull;

/**
 * 抛异常工具类
 * @author riang
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    public static void throwValidationException(BindingResult result) throws BusinessException {
        FieldError fieldError = result.getFieldError();
        throw new BusinessException(ErrorCode.PARAMS_ERROR, isNull(fieldError) ? "" : fieldError.getField() + fieldError.getDefaultMessage());
    }

    public static void checkRequestValid(BindingResult result) throws BusinessException{
        if (result.hasErrors()){
            throwValidationException(result);
        }
    }

    public static void checkRequestNotNull(Object request) throws BusinessException {
        ThrowUtils.throwIf(isNull(request), ErrorCode.PARAMS_ERROR, "请求参数不可为空");
    }
}
