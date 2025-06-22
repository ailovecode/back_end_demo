package edu.zut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zut.entity.Role;
import edu.zut.service.RoleService;
import edu.zut.mapper.RoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【roles】的数据库操作Service实现
* @createDate 2025-06-18 22:11:06
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Resource
    private RoleMapper roleMapper;

    @Override
    public void bindDefaultRole(Long userId) {

    }
}




