package edu.zut.service;

import edu.zut.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author DELL
* @description 针对表【roles】的数据库操作Service
* @createDate 2025-06-18 22:11:06
*/
public interface RoleService extends IService<Role> {

    void bindDefaultRole(Long userId);
}
