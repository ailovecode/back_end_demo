package edu.zut.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 日志类
 *
 * @TableName operation_logs
 */
@Data
public class OperationLog {
    private Long logId;

    private Long userId;

    private String action;

    private String ip;

    private String detail;
}