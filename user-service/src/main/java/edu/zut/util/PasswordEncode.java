package edu.zut.util;

import org.springframework.util.DigestUtils;

/**
 * 加密密码
 *
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/18 15:55
 */
public class PasswordEncode{

    private static final String SALT = "zhy";

    /**
     * 密码加密
     * @param rawPassword 登录时传入的密码
     */
    public static String encode(String rawPassword) {
        return DigestUtils.md5DigestAsHex((SALT + rawPassword).getBytes());
    }
    /**
     * 密码对比
     * @param rawPassword 登录时传入的密码
     * @param encodedPassword 数据库保存的加密过的密码
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encodedPassword.equals(DigestUtils.md5DigestAsHex((SALT + rawPassword).getBytes()));
    }
}
