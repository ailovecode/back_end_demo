package edu.zut.controller;


import edu.zut.entity.Result;
import edu.zut.service.RoleService;
import edu.zut.service.UserRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/18 21:01
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class PermissionController {

    @Resource
    private UserRoleService userRoleService;

    // 根据角色id查找所有角色
    @GetMapping("/getUserByRoleId")
    public Result<List<Long>> getUserByRoleId(@RequestParam("roleId") Integer roleId) {
        if(roleId < 1 || roleId > 3) {
            log.warn("角色id错误");
            return Result.failed("角色码错误");
        }
        List<Long> result = userRoleService.getUserByRoleId(roleId);
        if(result == null) {
            log.warn("未找到用户！");
            return Result.failed("未找到该角色用户");
        }
        return Result.success(result);
    }

    // 绑定默认角色（普通用户）
    @PostMapping("/setDefault")
    public Result<Integer> bindDefaultRole(@RequestParam("userId") Long userId){
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            return Result.failed("用户 ID 为空或为 0！");
        }
        // 判断用户是否已经存在
        Boolean flag = userRoleService.getUserRole(userId);
        if (flag) {
            return Result.failed("用户已存在！");
        }
        Integer result = userRoleService.bindDefaultRole(userId);
        if(result != 1) {
            return Result.failed("绑定用户失败！");
        }
        return Result.success(result);
    }

    // 查询用户角色码（返回role_code）
    @GetMapping("/getRoleCode")
    public Result<String> getUserRoleCode(@RequestParam("userId") Long userId) {
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            return Result.failed("用户 ID 为空或为 0！");
        }
        // 2. 执行服务查找
        String result = userRoleService.getUserRoleCode(userId);
        if(result == null) {
            return Result.failed("未获取到用户角色!");
        }
        return Result.success(result);
    }

    // 超管调用：升级用户为管理员
    @PostMapping("/upgradeToAdmin")
    public Result<Integer> upgradeToAdmin(@RequestParam("userId") Long userId) {
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            return Result.failed("用户 ID 为空或为 0！");
        }
        Integer role = userRoleService.checkPermission(userId);
        if(role == 1) {
            return Result.failed("对方是超管，你无权修改！");
        }
        // 2. 执行服务更新
        Integer result = userRoleService.upgradeToAdmin(userId);
        if(result <= 0) {
            return Result.failed("更新角色错误！");
        }
        return Result.success();
    }

    // 超管调用：降级用户为普通角色
    @PostMapping("/downgradeToUser")
    public Result<Integer> downgradeToUser(@RequestParam("userId") Long userId) {
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            return Result.failed("用户 ID 为空或为 0！");
        }
        Integer role = userRoleService.checkPermission(userId);
        if(role == 1) {
            return Result.failed("对方是超管，你无权修改！");
        }
        // 2. 执行服务更新
        Integer result = userRoleService.downgradeToUser(userId);
        if(result <= 0) {
            return Result.failed("更新角色错误！");
        }
        return Result.success();
    }
}
