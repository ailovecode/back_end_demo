package edu.zut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zut.common.ErrorCode;
import edu.zut.entity.RoleConstants;
import edu.zut.entity.UserRole;
import edu.zut.exception.BusinessException;
import edu.zut.mapper.RoleMapper;
import edu.zut.service.RoleService;
import edu.zut.service.UserRoleService;
import edu.zut.mapper.UserRoleMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author DELL
* @description 针对表【user_roles】的数据库操作Service实现
* @createDate 2025-06-18 22:16:05
*/
@Slf4j
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 为注册用户绑定默认角色 2 - 普通用户
     * @param userId
     * @return
     */
    @Override
    public Integer bindDefaultRole(Long userId) {

        // 判断是否是第一个用户
        List<UserRole> list = userRoleMapper.selectList(null);
        // 执行更新
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        if(list.isEmpty()) {
            // 第一个用户权限设为 超级管理员
            log.info("首位用户 {} 设为超级管理员 {}", userId, RoleConstants.SUPER_ADMIN);
            userRole.setRoleId(RoleConstants.SUPER_ADMIN);
        } else {
            // 否则设为 普通用户
            userRole.setRoleId(RoleConstants.USER);
        }
        int insert = userRoleMapper.insert(userRole);
        return insert;
    }

    /**
     * 查找用户的角色码
     * @param userId
     * @return
     */
    @Override
    public String getUserRoleCode(Long userId) {
        // 联表查找
        String result = userRoleMapper.selectRoleCode(userId);
        if(result == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR ,"未查找到用户角色信息");
        }
        return result;
    }

    /**
     * 检查用户的角色
     * @param userId
     * @return
     */
    @Override
    public Integer checkPermission(Long userId) {
        QueryWrapper<UserRole> queryUserRoleWrapper = new QueryWrapper<>();
        queryUserRoleWrapper.eq("user_id", userId);

        UserRole userRole = this.getOne(queryUserRoleWrapper);
        if(userRole == null) {
            log.warn("未找到 {} 用户的角色信息", userId);
            throw new BusinessException(ErrorCode.REQUEST_ERROR ,"未查到该用户！");
        }
        return userRole.getRoleId();
    }

    /**
     * 超管更新普通用户的角色为管理员
     * @param userId
     * @return
     */
    @Override
    public Integer upgradeToAdmin(Long userId) {
        UpdateWrapper<UserRole> updateUserRoleWrapper = new UpdateWrapper<>();
        updateUserRoleWrapper.eq("user_id", userId);
        updateUserRoleWrapper.set("role_id", RoleConstants.ADMIN);
        Integer result = userRoleMapper.update(updateUserRoleWrapper);
        if(result <= 0) {
            log.warn("更新为管理员错误！");
            throw new BusinessException(ErrorCode.REQUEST_ERROR ,"更新为管理员错误！");
        }
        return result;
    }

    /**
     * 超管更新管理员角色为普通用户
     * @param userId
     * @return
     */
    @Override
    public Integer downgradeToUser(Long userId) {
        UpdateWrapper<UserRole> updateUserRoleWrapper = new UpdateWrapper<>();
        updateUserRoleWrapper.eq("user_id", userId);
        updateUserRoleWrapper.set("role_id", RoleConstants.USER);
        Integer result = userRoleMapper.update(updateUserRoleWrapper);
        if(result <= 0) {
            log.warn("更新为普通用户错误！");
            throw new BusinessException(ErrorCode.REQUEST_ERROR ,"更新为普通用户错误！");
        }
        return result;
    }

    /**
     * 根据角色码查找所有用户
     * @param roleId
     * @return
     */
    @Override
    public List<Long> getUserByRoleId(Integer roleId) {
        QueryWrapper<UserRole> queryUserRoleWrapper = new QueryWrapper<>();
        queryUserRoleWrapper.eq("role_id", roleId);
        List<Long> userRoles = userRoleMapper.selectList(queryUserRoleWrapper).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());;
        return userRoles;
    }

    /**
     * 查找用户是否存在
     *
     * @param userId
     * @return
     */
    @Override
    public Boolean getUserRole(Long userId) {
        QueryWrapper<UserRole> queryUserRoleWrapper = new QueryWrapper<>();
        queryUserRoleWrapper.eq("user_id", userId);
        UserRole userRole = userRoleMapper.selectOne(queryUserRoleWrapper);
        if(userRole == null) {
            return false;
        }
        return true;
    }
}




