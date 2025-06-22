package edu.zut.service.impl;

import com.alibaba.fastjson.JSON;
import edu.zut.entity.OperationLog;
import edu.zut.service.MQProducerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 生产者服务
 *
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/22 8:35
 */
@Service
@Slf4j
public class MQProducerServiceImpl implements MQProducerService {

    private static final String topic = "LOGGING_TOPIC";

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    // 异步发送日志信息
    @Override
    public void sendAsync(OperationLog message) {
        Message<OperationLog> msg = MessageBuilder.withPayload(message)
                .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, "3") // 延迟级别
                .build();
        rocketMQTemplate.asyncSend(topic, msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("异步发送成功: {}", sendResult.getMsgId());
            }
            @Override
            public void onException(Throwable e) {
                log.info("异步发送失败: {}", e.getMessage());
            }
        });
    }
}
