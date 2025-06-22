package edu.zut.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
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
import edu.zut.util.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/17 20:54
 */
@Slf4j
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @PostMapping("/user/register")
    public Result<Integer> userRegister(@RequestBody UserRegister userRegisterRequest, HttpServletRequest request) {
        // 参数判空
        if (userRegisterRequest == null) {
            return Result.failed("注册信息为空!");
        }

        String username = userRegisterRequest.getUsername();
        String userPassword = userRegisterRequest.getPassword();
        String userPhone = userRegisterRequest.getPhone();
        String userEmail = userRegisterRequest.getEmail();
        if (StringUtils.isAnyBlank(username, userPassword, userPhone, userEmail)) {
            return Result.failed("注册信息参数为空!");
        }
        Boolean result = userService.userRegister(userRegisterRequest, request);
        if(!result) {
            return Result.failed("注册失败！");
        }
        return Result.success("注册成功",1);
    }

    @PostMapping("/user/login")
    public Result<String> userLogin(@RequestBody UserLogin userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return Result.failed("请求参数为空！");
        }

        // 校验账号密码
        User result = userService.userLogin(userLoginRequest, request);
        if(result == null) {
            return Result.failed("登录失败，用户名或密码错误！");
        }

        // 获取用户角色码
        String userRoleCode = permissionService.getUserRoleCode(result.getUserId());
        log.info("获取userRoleCode:{}", userRoleCode);
        // 生成Token
        String token = JWTUtils.generateToken(result.getUserId(),userRoleCode);

        return Result.success(token);
    }

    @GetMapping("/users")
    public Result<Page<User>> getPagingUserList(@Valid PageVo pageVo, HttpServletRequest req) {
        if(pageVo == null) {
            return Result.failed("请求参数为空！");
        }
        // 1. 鉴权
        UserVo userVo = (UserVo) req.getAttribute("userVo");
        if(userVo == null) {
            return Result.failed("请先登录！");
        }
        RoleEnum userRole = userService.getRole(userVo);
        log.info("登录用户 - Role:{}", userRole);
        // 2. 分权限查找所有用户列表并进行分页
        // 设置分页参数，从 第 1 页 开始，每页查 5 条数据
        Page<User> resultList = null;
        switch (userRole) {
            case USER:
                // 获得普通用户自己的信息
                resultList = userService.getUser(pageVo, userVo.getUserId(), req);
                break;
            case ADMIN:
                // 获取所有普通用户的信息
                resultList = userService.getAdminList(pageVo, userVo.getUserId(), req);
                break;
            case SUPER_ADMIN:
                // 获取所有用户的信息
                resultList = userService.getAllList(pageVo, userVo.getUserId(), req);
                break;
            default:
                throw new IllegalArgumentException("获取用户角色错误！");
        }
        if(resultList == null || resultList.getSize() == 0) {
            return Result.failed("未查到用户信息！");
        }
        return Result.success("查询用户成功", resultList);
    }

    @GetMapping("/user/{userId}")
    public Result<User> getUser(@PathVariable("userId") Long userId, HttpServletRequest req) {
        if(userId == null) {
            return Result.failed("请求参数为空！");
        }
        // 判断用户是否存在
        User findUser = userService.getUserById(userId);
        if(findUser == null) {
            return Result.failed("该用户不存在！");
        }

        // 1. 校验登录用户权限
        UserVo userVo = (UserVo) req.getAttribute("userVo");
        if(userVo == null) {
            return Result.failed("请先登录！");
        }
        RoleEnum userRole = userService.getRole(userVo);
        log.info("登录用户 - Role : {}", userRole);

        // 2. 判断查看用户权限
        Gson gson = new Gson();
        String userRoleCode = permissionService.getUserRoleCode(userId);
        Result result = gson.fromJson(userRoleCode, Result.class);
        userRoleCode = (String) result.getData();
        RoleEnum goalRole = RoleEnum.getByCode(userRoleCode);
        log.info("传入用户 - Role : {}", goalRole);
        User user = null;

        // 普通用户 只能查自己
        if(userRole == RoleEnum.USER && userRole == goalRole && userVo.getUserId().equals(userId)) {
            // 既是用户，也是自己的ID
            user = userService.getUserById(userId);
        }
        // 管理员 只能查普通用户 或者 自己
        else if((userRole == RoleEnum.ADMIN && goalRole == RoleEnum.USER) || userVo.getUserId().equals(userId)) {
            user = userService.getUserById(userId);
        }
        // 超管 可以查任何人
        else if(userRole == RoleEnum.SUPER_ADMIN) {
            user = userService.getUserById(userId);
        }
        // 其他情况，或是没有权限
        else {
            return Result.failed("没有权限查此用户！");
        }

        // 未查到
        if(user == null) {
            return Result.failed("获取该用户信息失败！");
        }
        return Result.success("查询成功！", user);
    }

    @PostMapping("/user/{userId}")
    public Result<Integer> updateUser (@PathVariable("userId") Long userId, @RequestBody UserRegister newUser, HttpServletRequest req) {
        if(userId == null || newUser == null) {
            return Result.failed("请求参数为空！");
        }
        // 判断用户是否存在
        User user = userService.getUserById(userId);
        if(user == null) {
            return Result.failed("该用户不存在！");
        }

        // 1. 校验登录用户权限
        UserVo userVo = (UserVo) req.getAttribute("userVo");
        if(userVo == null) {
            return Result.failed("请先登录 ！");
        }
        RoleEnum userRole = userService.getRole(userVo);
        log.info("登录用户 - Role : {}", userRole);

        // 2. 判断查看用户权限
        Gson gson = new Gson();
        String userRoleCode = permissionService.getUserRoleCode(userId);
        Result JsonResult = gson.fromJson(userRoleCode, Result.class);
        userRoleCode = (String) JsonResult.getData();
        RoleEnum goalRole = RoleEnum.getByCode(userRoleCode);
        log.info("传入用户 - Role : {}", goalRole);
        Integer result = 0;

        // 普通用户 只能修改自己
        if(userRole == RoleEnum.USER && userRole == goalRole && userVo.getUserId().equals(userId)) {
            // 既是用户，也是自己的ID
            result = userService.updateUser(userId, newUser, req);
        }
        // 管理员 只能查普通用户 或者 自己
        else if((userRole == RoleEnum.ADMIN && goalRole == RoleEnum.USER) || userVo.getUserId().equals(userId)) {
            result = userService.updateUser(userId, newUser, req);
        }
        // 超管 可以查任何人
        else if(userRole == RoleEnum.SUPER_ADMIN) {
            result = userService.updateUser(userId, newUser, req);
        }
        else {
            return Result.failed("没有权限更改此用户信息！");
        }

        if(result < 1) {
            return Result.failed("更新失败!");
        }
        return Result.success("更新成功！", result);
    }

    @PostMapping("/user/reset-password")
    public Result<Integer> resetPassword(@RequestParam("newPassword") String newPassword, HttpServletRequest req) {
        if("".equals(newPassword) || newPassword.isEmpty()) {
            return Result.failed("新密码为空！");
        }
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            return Result.failed("密码长度必须在6到20之间！");
        }

        // 1. 鉴权
        UserVo userVo = (UserVo) req.getAttribute("userVo");
        RoleEnum userRole = userService.getRole(userVo);
        log.info("登录用户 - Role : {}", userRole);
        Integer result = 0;

        // 2. 分权限查找所有用户列表并进行分页
        switch (userRole) {
            case USER:
                // 只能重置自己的
                result = userService.resetOwnerPassword(newPassword, userVo.getUserId(), req);
                break;
            case ADMIN:
                // 只能重置普通用户的
                result = userService.resetUserPassword(newPassword, userVo.getUserId(), req);
                break;
            case SUPER_ADMIN:
                // 重置所有人
                result = userService.resetAllPassword(newPassword, userVo.getUserId(), req);
                break;
            default:
                throw new IllegalArgumentException("获取用户角色错误！");
        }
        if(result < 1) {
            return Result.failed("重置失败，请检查数据");
        }
        return Result.success("重置密码成功！", result);
    }

}
