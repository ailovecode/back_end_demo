package edu.zut.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/18 21:05
 */
@FeignClient("permission-service")
public interface PermissionService {

    // 绑定默认角色（普通用户）
    @PostMapping("/auth/setDefault")
    String bindDefaultRole(@RequestParam("userId")Long userId);

    // 查询用户角色码（返回role_code）
    @GetMapping("/auth/getRoleCode")
    String getUserRoleCode(@RequestParam("userId") Long userId);

    // 根据角色id查找所有角色
    @GetMapping("/auth/getUserByRoleId")
    String getUserByRoleId(@RequestParam("roleId") Integer roleId);

    // 超管调用：升级用户为管理员
    @PostMapping("/auth/upgradeToAdmin")
    String upgradeToAdmin(@RequestParam("userId") Long userId);

    // 超管调用：降级用户为普通角色
    @PostMapping("/auth/downgradeToUser")
    String downgradeToUser(@RequestParam("userId") Long userId);
}
