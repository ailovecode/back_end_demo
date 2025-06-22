package edu.zut.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import edu.zut.entity.OperationLog;
import edu.zut.entity.User;
import edu.zut.entity.request.UserRegister;
import edu.zut.entity.vo.PageVo;
import edu.zut.mapper.UserMapper;
import edu.zut.util.SnowflakeUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/20 7:51
 */
@Slf4j
@SpringBootTest
public class UserRegisterServiceTest {

    @Autowired
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MQProducerService mqProducerService;

    @Test
    public void testLogin() {

        // 2. 构建日志消息
//        OperationLog msg = new OperationLog();
//        msg.setUserId(111111111111111111l);
//        msg.setAction("register_user");
//        msg.setIp("192.168.1.1");
//        msg.setDetail(
//                new Gson().toJson(Map.of(
//                        "username", "req.getUsername()",
//                        "email", "req.getEmail()",
//                        "phone", "req.getPhone()"
//                ))
//        );
//        mqProducerService.sendAsync(msg);


//        Map<String, OperationLog> headers = new HashMap<>();M

//        Message<OperationLog> message = new GenericMessage<>(msg);
//        boolean send = streamBridge.send("producer-out-0", message);
//        if(send) {
//            log.info("成功发送！");
//        } else {
//            log.info("发送失败！");
//        }
//        headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, );

        // 3B. 异步发送（推荐，避免阻塞主流程）
//        rocketMQTemplate.asyncSend(
//                "operation_log_topic",
//                MessageBuilder.withPayload(msg).build(),
//                new SendCallback() {
//                    @Override
//                    public void onSuccess(SendResult sendResult) {
//                        log.info("{} 日志保存成功！",sendResult);
//                        // 成功后可以记录日志或指标
//                    }
//                    @Override
//                    public void onException(Throwable e) {
//                        log.info("日志保存失败！");
//                        // 失败时可以重试或告警
//                    }
//                }
//        );

//        long id = SnowflakeUtils.get().id();
//        System.out.println(id);

        // 联表更新密码
//        Integer count = userService.resetUserPassword("123456");
//        System.out.println(count);
//        Page<User> userPage = userService.getAllList(new PageVo(1, 3));
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", 1935232973121380352l);
//
//        User user = userService.getOne(queryWrapper);
//        System.out.println(user);
        // 普通用户查询
//        Page<User> userPage = userService.getAllList(new PageVo(1, 3));

        // 管理员查询
//        MPJLambdaWrapper<User> lambdaWrapper  = new MPJLambdaWrapper<>();
//        lambdaWrapper.selectAll(User.class)
//                .leftJoin(UserRole.class, UserRole::getUserId, User::getUserId)
//                .eq(UserRole::getRoleId, 2);
//        Page<User> userPage = userMapper.selectJoinPage(new Page<>(1, 3), User.class, lambdaWrapper);


//        List<Long> userIds = new ArrayList<>(Arrays.asList(1935627355846729728l,1935859559098175492l,1935859216385769474l,1935859217186881538l));

//        MPJLambdaWrapper<User> lambdaWrapper  = new MPJLambdaWrapper<>();
//        lambdaWrapper.selectAll(User.class)
//                .in(UserRole::getUserId, userIds);
//        Page<User> userPage = userMapper.selectJoinPage(new Page<>(1, 3), User.class, lambdaWrapper);
//        if(userPage == null || userPage.getSize() == 0) {
//            throw new IllegalArgumentException("获取用户列表失败！");
//        }



        // 打印关键信息
//        System.out.println("===== 分页数据 =====");
//        System.out.println("当前页: " + userPage.getCurrent());
//        System.out.println("每页条数: " + userPage.getSize());
//        System.out.println("总记录数: " + userPage.getTotal());
//        System.out.println("总页数: " + userPage.getPages());
//
//        System.out.println("===== 当前页数据 =====");
//        userPage.getRecords().forEach(user -> {
//            System.out.println("用户ID: " + user.getUserId() +
//                    ", 姓名: " + user.getUsername()); // 联查字段
//        });
        // 超管查询
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
////        queryWrapper.eq("username", "zhy");
////        User user = userService.getOne(queryWrapper);
////        System.out.println(user);

        // 循环生成并注册10个用户
//        for (int i = 0; i < 1; i++) {
//            UserRegister user = new UserRegister();
//            // 使用带序号的用户名，方便追踪
//            String randomUsername = "batchUser_" + i + "_" + UUID.randomUUID().toString().substring(0, 4);
//            user.setUsername(randomUsername);
//            user.setEmail(randomUsername + "@example.com");
//            user.setPassword("validPassword" + i); // 密码也带序号
//            // 生成不同的手机号，最后4位用序号填充
//            user.setPhone("138" + String.format("%08d", i));
//
//            System.out.println(user);
//
//            Boolean result = userService.userRegister(user);
//
//            // 验证数据是否写入数据库
////            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
////            queryWrapper.eq("username", randomUsername);
////            User dbUser = userService.getOne(queryWrapper);
//        }
//
//        System.out.println("成功注册10个用户");
    }
}
