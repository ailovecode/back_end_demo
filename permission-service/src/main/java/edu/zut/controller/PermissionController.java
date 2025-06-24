package edu.zut.controller;


import edu.zut.common.BaseResponse;
import edu.zut.common.ErrorCode;
import edu.zut.common.ResultUtil;
import edu.zut.exception.BusinessException;
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
    public BaseResponse<List<Long>> getUserByRoleId(@RequestParam("roleId") Integer roleId) {
        if(roleId < 1 || roleId > 3) {
            log.warn("角色id错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"角色 id 错误");
        }
        List<Long> result = userRoleService.getUserByRoleId(roleId);
        if(result == null) {
            log.warn("未找到用户！");
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"未找到用户！");
        }
        return ResultUtil.success(result);
    }

    // 绑定默认角色（普通用户）
    @PostMapping("/setDefault")
    public BaseResponse<Integer> bindDefaultRole(@RequestParam("userId") Long userId){
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 ID 为空或为 0！");
        }
        // 判断用户是否已经存在
        Boolean flag = userRoleService.getUserRole(userId);
        if (flag) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"用户已存在！");
        }
        Integer result = userRoleService.bindDefaultRole(userId);
        if(result != 1) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR, "绑定用户失败！");
        }
        return ResultUtil.success(result);
    }

    // 查询用户角色码（返回role_code）
    @GetMapping("/getRoleCode")
    public BaseResponse<String> getUserRoleCode(@RequestParam("userId") Long userId) {
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 ID 为空或为 0！");
        }
        // 2. 执行服务查找
        String result = userRoleService.getUserRoleCode(userId);
        if(result == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR, "未获取到用户角色!");
        }
        return ResultUtil.success(result);
    }

    // 超管调用：升级用户为管理员
    @PostMapping("/upgradeToAdmin")
    public BaseResponse<Integer> upgradeToAdmin(@RequestParam("userId") Long userId) {
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 ID 为空或为 0！");
        }
        Integer role = userRoleService.checkPermission(userId);
        if(role == 1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "对方是超管，你无权修改！");
        }
        // 2. 执行服务更新
        Integer result = userRoleService.upgradeToAdmin(userId);
        if(result <= 0) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR, "更新角色错误！");
        }
        return ResultUtil.success(result);
    }

    // 超管调用：降级用户为普通角色
    @PostMapping("/downgradeToUser")
    public BaseResponse<Integer> downgradeToUser(@RequestParam("userId") Long userId) {
        // 1. 验证用户 ID 是否正确传入
        if(userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 ID 为空或为 0！");
        }
        Integer role = userRoleService.checkPermission(userId);
        if(role == 1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "对方是超管，你无权修改！");
        }
        // 2. 执行服务更新
        Integer result = userRoleService.downgradeToUser(userId);
        if(result <= 0) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR, "更新角色错误！");
        }
        return ResultUtil.success(result);
    }
}
