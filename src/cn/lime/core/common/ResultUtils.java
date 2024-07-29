package cn.lime.core.common;

/**
 * 返回工具类
 */
public class ResultUtils {
    public static BaseResponse<Void> successOr(boolean isSuccess, ErrorCode errorCode){
        if(isSuccess){
            return success(null);
        }else{
            return error(errorCode);
        }
    }

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data,String message) {
        return new BaseResponse<>(0, data, message);
    }

    /**
     * 失败
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}