package edu.zut.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName users_copy0
 */
@Table(name = "user")
@Data
@Accessors(chain = true)
public class User {
    /**
     * id
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 邮件
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;
}