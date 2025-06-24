package edu.zut.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/24 10:40
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 42L;
    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(){};

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data,String message) {
        this(code, data, message ,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode,T data,String message,String description) {
        this(errorCode.getCode(), data, message, description);
    }

    public BaseResponse(ErrorCode errorCode,T data,String message) {
        this(errorCode.getCode(), data, message, errorCode.getDescription());
    }
}
