package edu.zut.service;

import edu.zut.entity.OperationLog;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/22 8:35
 */
public interface MQProducerService {
    void sendAsync(OperationLog message);
}
