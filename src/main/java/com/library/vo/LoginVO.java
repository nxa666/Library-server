package com.library.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 登录返回VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private String token;

    private Long id;

    private String username;

    private String role;
}
