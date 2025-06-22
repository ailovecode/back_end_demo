package edu.zut.entity;

/**
 * 用户角色枚举（线程安全、不可变）
 *  AI
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/19 15:54
 */
public enum RoleEnum {

    USER("user"),
    ADMIN("admin"),
    SUPER_ADMIN("super_admin");

    // 枚举值编码（与原常量值保持一致）
    private final String code;

    /**
     * 私有构造（默认private可不写，显式写出更清晰）
     */
    RoleEnum(String code) {
        this.code = code;
    }

    // ---------- 安全访问方法 ----------
    public String getCode() {
        return this.code;
    }

    /**
     * 通过code获取枚举（安全替代valueOf）
     * @param code 枚举编码
     * @return 匹配的枚举，未找到返回null
     */
    public static RoleEnum getByCode(String code) {
        for (RoleEnum role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        return null; // 或抛出 IllegalArgumentException
    }
}