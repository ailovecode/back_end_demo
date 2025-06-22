package edu.zut.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 写入 Token 的用户信息
 *
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/20 9:01
 */
@Data
@AllArgsConstructor
public class UserVo {


    public Long userId;
    public String role;
}
