package edu.zut.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import edu.zut.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author DELL
* @description 针对表【users_copy0】的数据库操作Mapper
* @createDate 2025-06-17 20:52:34
* @Entity edu.zut.entity.User
*/
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {
}




