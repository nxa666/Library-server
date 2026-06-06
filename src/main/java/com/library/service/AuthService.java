package com.library.service;

import com.library.dto.LoginDTO;
import com.library.dto.RegisterDTO;
import com.library.vo.LoginVO;
import com.library.vo.UserInfoVO;

/**
 * 认证Service
 */
public interface AuthService {

    /**
     * 注册
     */
    void register(RegisterDTO dto);

    /**
     * 登录
     */
    LoginVO login(LoginDTO dto);

    /**
     * 获取当前用户信息
     */
    UserInfoVO getCurrentUser(Long userId);
}
