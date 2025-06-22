package edu.zut.mapper;

import edu.zut.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author DELL
* @description 针对表【user_roles】的数据库操作Mapper
* @createDate 2025-06-18 22:16:05
* @Entity edu.zut.entity.UserRole
*/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    String selectRoleCode(@Param("userId") Long userId);
}




