package edu.zut.exception;

import edu.zut.common.BaseResponse;
import edu.zut.common.ErrorCode;
import edu.zut.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/24 10:42
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.info("业务异常：{}", e.getDescription());
        return ResultUtil.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("系统运行时异常：", e);
        // 运行时异常没有 getDescription()，可以只返回 message
        return ResultUtil.error(
                ErrorCode.SYSTEM_ERROR,
                e.getMessage(),
                "系统内部异常，请稍后重试"
        );
    }

    // 捕获所有 Exception：
    @ExceptionHandler(Exception.class)
    public BaseResponse exceptionHandler(Exception e) {
        log.error("通用异常：", e);
        return ResultUtil.error(
                ErrorCode.SYSTEM_ERROR,
                e.getMessage(),
                "未知错误，请联系管理员"
        );
    }
}
