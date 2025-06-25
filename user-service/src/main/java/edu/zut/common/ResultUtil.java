package edu.zut.common;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/24 10:37
 */
public class ResultUtil {

    private ResultUtil() {

    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(2000,null,"响应成功！");
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(2000,data,"响应成功！");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse<Void> error(int code,String message,String description){
        return new BaseResponse<>(code,null,message,description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse<Void> error(ErrorCode errorCode,String message,String description) {
        return new BaseResponse<>(errorCode.getCode(),null,message,description);
    }
}
