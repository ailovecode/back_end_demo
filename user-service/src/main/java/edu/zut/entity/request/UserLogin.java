package edu.zut.entity.request;

import lombok.Data;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/18 14:13
 */
@Data
public class UserLogin {

    private static final long serialVersionUID = -4780927654973334115L;

    private String username;
    private String password;
}
