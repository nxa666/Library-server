package com.library.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * 用户DTO（新增/编辑）
 */
@Data
public class UserDTO {

    private String username;

    private String password;

    private String realName;

    @Pattern(regexp = "^\\d{8,30}$", message = "学号格式不正确")
    private String studentNo;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String avatar;

    private String role;       // READER / ADMIN

    private Integer status;
}
