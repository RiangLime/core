package cn.lime.core._common;

/**
 * @ClassName: BusinessCatchableException
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/4/20 18:39
 */
public class BusinessCatchableException extends Exception {

    /**
     * 错误码
     */
    private final int code;

    public BusinessCatchableException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessCatchableException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessCatchableException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
