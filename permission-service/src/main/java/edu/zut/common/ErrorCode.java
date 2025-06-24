package edu.zut.common;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/24 10:39
 */
public enum ErrorCode {
    /**
     * 请求类型
     */
    SUCCESS(2000, "ok", ""),
    REQUEST_ERROR(5000,"请求错误",""),
    PARAMS_ERROR(5001, "请求参数错误", ""),
    NULL_ERROR(5002, "请求数据为空", ""),
    NOT_LOGIN(5003, "未登录", ""),
    NO_AUTH(5004, "无权限", ""),
    SYSTEM_ERROR(4000,"系统内部异常","");

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String message;
    /**
     * 错误描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

}
