package edu.zut.mapper;

import edu.zut.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author DELL
* @description 针对表【roles】的数据库操作Mapper
* @createDate 2025-06-18 22:11:06
* @Entity edu.zut.entity.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}




