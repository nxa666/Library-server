package com.library.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    private Long id;

    private String username;

    private String realName;

    private String email;

    private String phone;

    private String avatar;

    private String role;
}
