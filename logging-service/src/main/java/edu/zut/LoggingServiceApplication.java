package edu.zut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.function.Consumer;


/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/17 18:27
 */
@SpringBootApplication
@MapperScan("edu.zut.mapper")
@EnableDiscoveryClient //启用服务发现注解
public class LoggingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoggingServiceApplication.class, args);
    }

}
