package edu.zut.controller;

import edu.zut.entity.OperationLog;
import edu.zut.service.OperationLogService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/22 10:31
 */
@RestController
@RequestMapping("/log")
public class LoggingController {

    private static final Logger log = LoggerFactory.getLogger(LoggingController.class);
    @Resource
    private OperationLogService operationLogService;

    @PostMapping("/storage")
    public void logStorage(@RequestBody OperationLog operationLog) {
        log.info("日志消息: {}", operationLog);
        operationLogService.save(operationLog);
    }

}
