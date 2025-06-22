package edu.zut.service;

import edu.zut.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author DELL
* @description 针对表【user_roles】的数据库操作Service
* @createDate 2025-06-18 22:16:05
*/
public interface UserRoleService extends IService<UserRole> {

    Integer bindDefaultRole(Long userId);

    String getUserRoleCode(Long userId);

    Integer checkPermission(Long userId);

    Integer upgradeToAdmin(Long userId);

    Integer downgradeToUser(Long userId);

    List<Long> getUserByRoleId(Integer roleId);

    Boolean getUserRole(Long userId);
}
