package edu.zut.service;

import edu.zut.entity.OperationLog;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 *
 * 消费者服务
 *
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/22 8:39
 */
@Service
@Slf4j
@RocketMQMessageListener(
        topic = "LOGGING_TOPIC",
        consumerGroup = "logging-consumer",
        selectorExpression = "*"
)
public class MQConsumerService implements RocketMQListener<OperationLog>{

    @Resource
    private LoggingService loggingService;

    @Override
    public void onMessage(OperationLog message) {
        log.info("异步消费的消息 {}", message);
        loggingService.logStorage(message);
    }
}
