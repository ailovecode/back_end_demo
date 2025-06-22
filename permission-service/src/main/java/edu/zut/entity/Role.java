package edu.zut.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName roles
 */
@TableName(value ="roles")
@Data
public class Role {
    /**
     * 角色ID
     */
    @TableId(value = "role_id")
    private Integer roleId;

    /**
     * 角色码
     */
    @TableField(value = "role_code")
    private String roleCode;
}