package edu.zut.service;

import com.alibaba.fastjson.JSON;
import edu.zut.util.SnowflakeUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/22 8:28
 */
@SpringBootTest
@Component
@Slf4j
public class MQProducerServiceTest {

    // 建议正常规模项目统一用一个TOPIC
    private static final String topic = "LOGGING_TOPIC";

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 发送同步消息（阻塞当前线程，等待broker响应发送结果，这样不太容易丢失消息）
     * （msgBody也可以是对象，sendResult为返回的发送结果）
     */
    @Test
    public void sendMsg() {
        for(int i = 0;i < 10;i++) {
            long id = SnowflakeUtils.get().id();
            System.out.println(id + " -- " + id % 4);
        }

    }
}
