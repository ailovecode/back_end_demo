package edu.zut.entity;

/**
 * 常量定义
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/19 15:54
 */
public class RoleConstants {
    private RoleConstants() {} // 防止实例化

    /** 普通用户 */
    public static final int USER = 2;
    /** 普通管理员（受限） */
    public static final int ADMIN = 3;
    /** 超级管理员 */
    public static final int SUPER_ADMIN = 1;
}
