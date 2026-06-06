package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.dto.LoginDTO;
import com.library.dto.RegisterDTO;
import com.library.entity.User;
import com.library.mapper.UserMapper;
import com.library.service.AuthService;
import com.library.utils.JwtUtils;
import com.library.vo.LoginVO;
import com.library.vo.UserInfoVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证Service实现
 */
@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDTO dto) {
        // 用户名重复校验
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        Long count = this.count(wrapper);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 构建用户对象
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole("READER");
        user.setStatus(1);

        // 保存用户
        this.save(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        // 根据username查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = this.getOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // BCrypt验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成JWT
        String token = jwtUtils.createToken(user.getId());

        // 返回LoginVO
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRole(user.getRole());
        return vo;
    }

    @Override
    public UserInfoVO getCurrentUser(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserInfoVO vo = new UserInfoVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setAvatar(user.getAvatar());
        vo.setRole(user.getRole());
        return vo;
    }
}
