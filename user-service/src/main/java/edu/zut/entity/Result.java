package edu.zut.entity;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author zhy
 * @param <T>
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(Integer code, String message) {
        this(code, message, null);
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        Result response = new Result(200,"操作成功");
        return response;
    }
//    public static <T> Result<T> success(String message) {
//        Result response = new Result(200,message);
//        return response;
//    }
    public static <T> Result<T> success(T date) {
        Result response = new Result(200,"操作成功",date);
        return response;
    }
    public static <T> Result<T> success(String message, T date) {
        Result response = new Result(200,message,date);
        return response;
    }

    public static <T> Result<T> failed() {
        Result response = new Result(500, "操作失败");
        return response;
    }

    public static <T> Result<T> failed(String message) {
        Result response = new Result(500, message);
        return response;
    }

    public static <T> Result<T> failed(Integer code, String message, T date) {
        Result response = new Result(code,message,date);
        return response;
    }

    public static <T> Result<T> failed(Integer code, String message) {
        Result response = new Result(code,message);
        return response;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
