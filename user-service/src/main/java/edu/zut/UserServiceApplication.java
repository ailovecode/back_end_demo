package edu.zut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/17 20:11
 */
// 远程调用服务
@EnableFeignClients
@SpringBootApplication
// 开启服务注册与发现
@EnableDiscoveryClient
@MapperScan("edu.zut.mapper")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
