package edu.zut.service;

import edu.zut.entity.OperationLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/22 10:36
 */
@FeignClient("logging-service")
public interface LoggingService {

    @PostMapping("/log/storage")
    void logStorage(@RequestBody OperationLog operationLog);
}
