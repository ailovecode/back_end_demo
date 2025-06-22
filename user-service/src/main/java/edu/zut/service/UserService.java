package edu.zut.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.zut.entity.RoleEnum;
import edu.zut.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.zut.entity.request.UserLogin;
import edu.zut.entity.request.UserRegister;
import edu.zut.entity.vo.PageVo;
import edu.zut.entity.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author DELL
* @description 针对表【users_copy0】的数据库操作Service
* @createDate 2025-06-17 20:52:34
*/
public interface UserService extends IService<User> {

    Boolean userRegister(UserRegister user, HttpServletRequest request);

    User userLogin(UserLogin userLoginRequest, HttpServletRequest request);

    Page<User> getUser(PageVo pageVo, Long userId, HttpServletRequest request);

    Page<User> getAdminList(PageVo pageVo, Long userId, HttpServletRequest request);

    Page<User> getAllList(PageVo pageVo, Long userId, HttpServletRequest request);

    RoleEnum getRole(UserVo userVo);

    User getUserById(Long userId);

    Integer updateUser(Long userId, UserRegister newUser, HttpServletRequest request);

    Integer resetUserPassword(String newPassword, Long userId, HttpServletRequest request);

    Integer resetAllPassword(String newPassword, Long userId,  HttpServletRequest request);

    Integer resetOwnerPassword(String newPassword, Long userId, HttpServletRequest req);
}
