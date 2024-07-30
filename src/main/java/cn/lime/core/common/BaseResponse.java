package cn.lime.core.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author riang
 * @param <T>
 */
@Data
@Schema(description = "通用返回类")
public class BaseResponse<T> implements Serializable {
    @Schema(description = "请求ID")
    private String reqId;
    @Schema(description = "状态码", defaultValue = "0")
    private int code;
    @Schema(description = "返回数据,成功返回null", defaultValue = "null")
    private T data;
    @Schema(description = "错误信息", defaultValue = "ok")
    private String message;

    public BaseResponse(){}

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

}