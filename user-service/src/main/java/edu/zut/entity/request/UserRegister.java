package edu.zut.entity.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/18 14:13
 */
@Data
public class UserRegister implements Serializable {

    private static final long serialVersionUID = 7049702280339494770L;

    private String username;
    private String password;
    private String email;
    private String phone;
}
