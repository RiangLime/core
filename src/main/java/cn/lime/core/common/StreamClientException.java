package cn.lime.core.common;

/**
 * @ClassName: StreamClientException
 * @Description: AI流式异常
 * @Author: Lime
 * @Date: 2023/12/4 10:55
 */
public class StreamClientException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    public StreamClientException(int code, String message) {
        super(message);
        this.code = code;
    }

    public StreamClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public StreamClientException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }

}
