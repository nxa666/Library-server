package com.library.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用户VO（返回前端）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private Long id;

    private String username;

    private String realName;

    private String studentNo;

    private String email;

    private String phone;

    private String avatar;

    private String role;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
