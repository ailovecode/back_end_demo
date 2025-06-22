package edu.zut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName operation_logs
 */
@TableName(value ="operation_logs")
@Data
public class OperationLog {
    /**
     * 日志id
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /**
     * 操作用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 行为
     */
    @TableField(value = "action")
    private String action;

    /**
     * ip地址
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 操作详情
     */
    @TableField(value = "detail")
    private String detail;
}