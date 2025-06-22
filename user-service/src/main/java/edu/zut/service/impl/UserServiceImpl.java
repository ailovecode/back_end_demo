package edu.zut.service.impl;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.gson.Gson;
import edu.zut.entity.OperationLog;
import edu.zut.entity.Result;
import edu.zut.entity.RoleEnum;
import edu.zut.entity.User;
import edu.zut.entity.request.UserLogin;
import edu.zut.entity.request.UserRegister;
import edu.zut.entity.vo.PageVo;
import edu.zut.entity.vo.UserVo;
import edu.zut.service.MQProducerService;
import edu.zut.service.PermissionService;
import edu.zut.service.UserService;
import edu.zut.mapper.UserMapper;
import edu.zut.util.PasswordEncode;
import edu.zut.util.SnowflakeUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author DELL
* @description 针对表【users_copy0】的数据库操作Service实现
* @createDate 2025-06-17 20:52:34
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionService permissionService;

    @Resource
    private MQProducerService mqProducerService;

    /**
     * 用户注册
     *
     * @param user
     * @param request
     * @return
     */
    @Override
    public Boolean userRegister(UserRegister user, HttpServletRequest request) {

        // 校验用户注册信息是否完善
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            log.warn("注册失败 – 用户名为空");
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            log.warn("注册失败 – 邮箱格式不正确: email={}", user.getEmail());
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            log.warn("注册失败 – 密码长度不合法: length={}", user.getPassword().length());
            throw new IllegalArgumentException("密码长度必须在6到20之间");
        }
        if (user.getPhone().length() != 11) {
            log.warn("注册失败 – 手机号格式不正确: phone={}", user.getPhone());
            throw new IllegalArgumentException("手机号格式不对");
        }

        // 设置日志对象
        OperationLog msg = new OperationLog();
        msg.setAction("register_user");
        msg.setIp(request.getRemoteAddr());

        // 判断是否已经存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User one = this.getOne(queryWrapper);
        if (one != null) {
            log.warn("注册失败 – 用户已存在: username={}", user.getUsername());
            throw new IllegalArgumentException("该用户已经注册过了！");
        }

        // 注册逻辑
        Long userId = SnowflakeUtils.get().id();
        String md5UserPassword = PasswordEncode.encode(user.getPassword());
        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setPassword(md5UserPassword);
        newUser.setGmtCreate(new Date());
        boolean result = this.save(newUser);

        // 设置权限
        log.info("开始绑定默认角色给用户 userId={}", userId);
        String json = permissionService.bindDefaultRole(userId);
        Result obj = JacksonUtils.toObj(json, Result.class);
        log.info("权限绑定结果: {}", json);
        if (obj.getCode() == 500) {
            log.error("角色绑定失败 – userId={}, response={}", userId, json);
            throw new IllegalArgumentException("用户角色绑定失败！");
        }
        if (!result) {
            log.error("注册失败 – 用户保存失败, userId={}", userId);
            return false;
        }

        // 异步消费注册日志
        msg.setUserId(userId);
        msg.setDetail(
                new Gson().toJson(Map.of(
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "phone", user.getPhone(),
                        "role", "user"
                ))
        );
        mqProducerService.sendAsync(msg);

        return true;
    }


    /**
     * 用户登录验证
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Override
    public User userLogin(UserLogin userLoginRequest, HttpServletRequest request) {

        // 校验用户登录信息
        if (userLoginRequest.getUsername() == null || userLoginRequest.getUsername().isEmpty()) {
            log.warn("登录失败 – 用户名为空");
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (userLoginRequest.getPassword().length() < 6 || userLoginRequest.getPassword().length() > 20) {
            log.warn("登录失败 – 密码长度不合法: length={}",
                    userLoginRequest.getPassword().length());
            throw new IllegalArgumentException("密码长度必须在6到20之间");
        }

        // 构建日志消息常量部分
        OperationLog msg = new OperationLog();
        msg.setAction("login_user");
        msg.setIp(request.getRemoteAddr());

        // 查询用户数据
        String md5Password = PasswordEncode.encode(userLoginRequest.getPassword());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", userLoginRequest.getUsername());
        wrapper.eq("password", md5Password);

        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            log.warn("登录失败 – 用户名或密码错误: username={}",
                    userLoginRequest.getUsername());
        } else {
            msg.setUserId(user.getUserId());
            msg.setDetail(new Gson().toJson(Map.of("success", "用户登录成功")));
            mqProducerService.sendAsync(msg);
            log.info("登录成功 – userId={}, username={}",
                    user.getUserId(), user.getUsername());
        }
        return user;
    }


    /**
     * 通过 UserId 获取登录用户的信息
     *
     * @param userId@return
     */
    @Override
    public Page<User> getUser(PageVo pageVo, Long userId, HttpServletRequest request) {
        log.info("普通用户 查询自己的信息");
        Page<User> userPage = userMapper.selectPage(new Page<>(pageVo.getPageNumber(), pageVo.getPageSize()), new QueryWrapper<User>().eq("user_id", userId));
        // 异步消费注册日志
        OperationLog msg = new OperationLog();
        msg.setUserId(userId);
        msg.setAction("get_own_message");
        msg.setIp(request.getRemoteAddr());
        if(userPage == null || userPage.getSize() == 0) {
            msg.setDetail(new Gson().toJson(Map.of("fail", "查询为空！")));
            mqProducerService.sendAsync(msg);
        } else {
            msg.setDetail(
                    new Gson().toJson(Map.of(
                            "pageNumber", userPage.getPages(),
                            "pageSize", userPage.getSize(),
                            "pageTotal", userPage.getTotal(),
                            "roleFilter", "user"
                    ))
            );
            mqProducerService.sendAsync(msg);
        }
        return userPage;
    }

    /**
     * 通过 RPC 服务调用查询所有普通用户，再执行分页查询所有普通用户信息
     * 普通用户角色id - 2
     *
     * @param pageVo
     * @return
     */
    @Override
    public Page<User> getAdminList(PageVo pageVo, Long userId, HttpServletRequest request) {

        log.info("管理员 获取<普通用户>的 ID 列表");
        List<Long> userIds = getUserIdsByRPC(2);

        // 异步消费注册日志
        OperationLog msg = new OperationLog();
        msg.setUserId(userId);
        msg.setAction("get_users_list");
        msg.setIp(request.getRemoteAddr());

        log.info("开始执行分页查询，过滤 user_id ∈ {}", userIds);
        MPJLambdaWrapper<User> lambdaWrapper  = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(User.class)
                .in(User::getUserId, userIds);

        Page<User> userPage = userMapper.selectJoinPage(new Page<>(pageVo.getPageNumber(), pageVo.getPageSize()), User.class, lambdaWrapper);

        if(userPage == null || userPage.getSize() == 0) {
            log.info("获取普通用户列表为空！");
            msg.setDetail(new Gson().toJson(Map.of("fail", "查询为空！")));
            mqProducerService.sendAsync(msg);
        } else {
            log.info("获取普通用户列表成功！");
            msg.setDetail(
                new Gson().toJson(Map.of(
                    "pageNumber", userPage.getPages(),
                    "pageSize", userPage.getSize(),
                    "pageTotal", userPage.getTotal(),
                    "roleFilter", "admin"
                ))
            );
            mqProducerService.sendAsync(msg);
        }
        return userPage;
    }

    /**
     * 通过MPJLambdaWrapper来执行分页联表查询所有用户信息
     *
     * @param pageVo
     * @param userId
     * @return
     */
    @Override
    public Page<User> getAllList(PageVo pageVo, Long userId, HttpServletRequest request) {

        log.info("超级管理员 获取所有用户列表！");
        Page<User> userPage = userMapper.selectPage(new Page<>(pageVo.getPageNumber(), pageVo.getPageSize()), null);

        // 异步消费注册日志
        OperationLog msg = new OperationLog();
        msg.setUserId(userId);
        msg.setAction("get_all_users_list");
        msg.setIp(request.getRemoteAddr());

        if(userPage == null || userPage.getSize() == 0) {
            log.info("获取所有用户列表为空！");
            msg.setDetail(new Gson().toJson(Map.of("fail", "查询为空！")));
            mqProducerService.sendAsync(msg);
        } else {
            log.info("获取所有用户列表成功！");
            msg.setDetail(
                    new Gson().toJson(Map.of(
                            "pageNumber", userPage.getPages(),
                            "pageSize", userPage.getSize(),
                            "pageTotal", userPage.getTotal(),
                            "roleFilter", "admin"
                    ))
            );
            mqProducerService.sendAsync(msg);
        }
        return userPage;
    }

    /**
     * 解析 role token
     * @param userVo
     * @return
     */
    @Override
     public RoleEnum getRole(UserVo userVo) {
        log.info("解析 JSON 获取用户角色");
        String roleResponse = userVo.getRole();
        Result roleResult = JacksonUtils.toObj(roleResponse, Result.class);
        String userRole = (String) roleResult.getData();
        RoleEnum role = RoleEnum.getByCode(userRole);
        return role;
    }

    /**
     * 通过 ID 获取用户信息
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 根据 userId 更新用户信息
     *
     * @param userId
     * @param newUser
     * @return
     */
    @Override
    public Integer updateUser(Long userId, UserRegister newUser, HttpServletRequest request) {
        log.info("开始更新用户信息 – userId={}", userId);

        // 构建更新包装器
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.eq("user_id", userId);

        // 校验与设置字段
        if (!newUser.getUsername().isEmpty()) {
            log.info("更新字段 username -> {}", newUser.getUsername());
            uw.set("username", newUser.getUsername());
        }
        if (!newUser.getEmail().isEmpty()) {
            if (!newUser.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                log.warn("邮箱格式不正确: {}", newUser.getEmail());
                throw new IllegalArgumentException("邮箱格式不正确");
            }
            uw.set("email", newUser.getEmail());
        }
        if (!newUser.getPhone().isEmpty()) {
            if (newUser.getPhone().length() != 11) {
                log.warn("手机号格式不正确: {}", newUser.getPhone());
                throw new IllegalArgumentException("手机号格式不对");
            }
            uw.set("phone", newUser.getPhone());
        }
        if (!newUser.getPassword().isEmpty()) {
            if (newUser.getPassword().length() < 6 || newUser.getPassword().length() > 20) {
                log.warn("密码长度不合法: {}", newUser.getPassword().length());
                throw new IllegalArgumentException("密码长度必须在6到20之间");
            }
            String md5 = PasswordEncode.encode(newUser.getPassword());
            uw.set("password", md5);
        }

        // 获取用户旧信息
        User oldUser = this.getById(userId);

        // 执行更新
        log.info("执行数据库更新: {}", uw.getSqlSegment());
        int updated = userMapper.update(null, uw);

        // 获取用户旧信息
        User updatedUser = this.getById(userId);

        // 构建并发送操作日志
        OperationLog msg = new OperationLog();
        msg.setUserId(userId);
        msg.setAction("update_user");
        msg.setIp(request.getRemoteAddr());
        msg.setDetail(new Gson().toJson(Map.of(
                "field", "userMessage",
                "oldUser", oldUser.toString(),  // 可具体列出修改字段
                "newUser", updatedUser.toString()
        )));
        mqProducerService.sendAsync(msg);

        return updated;
    }


    /**
     * 更新所有普通用户的密码
     * @param newPassword
     * @return
     */
    @Override
    public Integer resetUserPassword(String newPassword, Long userId, HttpServletRequest request) {
        // 调用 RPC 拿到普通用户 ID 列表
        log.info("调用 RPC 获取普通用户的 userIds 列表");
        List<Long> userIds = getUserIdsByRPC(2);

        if (!userIds.isEmpty()) {
            // 进行密码更新
            String md5 = PasswordEncode.encode(newPassword);

            UpdateWrapper<User> uw = new UpdateWrapper<>();
            uw.in("user_id", userIds)
                    .set("password", md5);

            int updated = userMapper.update(null, uw);
            log.info("批量重置密码完成 – affectedRows={}", updated);

            // 构建操作日志并发送
            OperationLog msg = new OperationLog();
            msg.setUserId(userId);
            msg.setAction("reset_users_password");
            msg.setIp(request.getRemoteAddr());
            msg.setDetail(new Gson().toJson(Map.of(
                    "field", "password",
                    "newPassword", "[newPassword]"
            )));
            mqProducerService.sendAsync(msg);

            return updated;
        } else {
            log.warn("无普通用户可重置密码，普通用户列表为空");
            return 0;
        }
    }

    /**
     * 修改自己密码
     *
     * @param newPassword
     * @return
     */
    @Override
    public Integer resetOwnerPassword(String newPassword, Long userId, HttpServletRequest request) {
        log.info("普通用户 执行重置自己的密码！");

        String md5 = PasswordEncode.encode(newPassword);
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.set("password", md5)
                .eq("user_id", userId);
        int result = userMapper.update(uw);

        // 构建操作日志并发送
        OperationLog msg = new OperationLog();
        msg.setUserId(userId);
        msg.setAction("reset_owner_password");
        msg.setIp(request.getRemoteAddr());
        msg.setDetail(new Gson().toJson(Map.of(
                "field", "password",
                "newPassword", "[newPassword]"
        )));
        mqProducerService.sendAsync(msg);
        return result;
    }

    /**
     * 超管重置所有用户的密码
     *
     * @param newPassword
     * @param userId
     * @param req
     * @return
     */
    @Override
    public Integer resetAllPassword(String newPassword, Long userId, HttpServletRequest req) {
        log.info("超级管理员 执行重置所有用户的密码！");

        String md5 = PasswordEncode.encode(newPassword);
        LambdaUpdateWrapper<User> uw = new LambdaUpdateWrapper<>();
        uw.set(User::getPassword, md5);
        int result = userMapper.update(null, uw);

        // 构建操作日志并发送
        OperationLog msg = new OperationLog();
        msg.setUserId(userId);
        msg.setAction("reset_all_users_password");
        msg.setIp(req.getRemoteAddr());
        msg.setDetail(new Gson().toJson(Map.of(
                "field", "password",
                "newPassword", "[newPassword]"
        )));
        mqProducerService.sendAsync(msg);
        return result;
    }

    /**
     * 通过 RPC 服务调用所有指定用户角色的用户id
     * @param roleId
     * @return
     */
    private List<Long> getUserIdsByRPC(Integer roleId) {
        String jsonResult = permissionService.getUserByRoleId(roleId);
        Result response = JacksonUtils.toObj(jsonResult, Result.class);
        if(response.getCode() == 500) {
            throw new IllegalArgumentException(response.getMessage());
        }
        List<Long> userIds = (List<Long>) response.getData();
        return userIds;
    }
}