package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.dto.UserDTO;
import com.library.entity.User;
import com.library.mapper.UserMapper;
import com.library.service.UserService;
import com.library.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 用户Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<UserVO> pageUsers(Long current, Long size, String username) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(username)) {
            wrapper.like(User::getUsername, username);
        }

        Page<User> userPage = this.page(page, wrapper);

        Page<UserVO> voPage = new Page<>(current, size);
        voPage.setTotal(userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public void addUser(UserDTO dto) {
        // 用户名重复校验
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        Long count = this.count(wrapper);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        this.save(user);
    }

    @Override
    public void updateUser(Long id, UserDTO dto) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 如果修改了用户名，检查是否重复
        if (!user.getUsername().equals(dto.getUsername())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, dto.getUsername()).ne(User::getId, id);
            Long count = this.count(wrapper);
            if (count > 0) {
                throw new RuntimeException("用户名已存在");
            }
        }

        BeanUtils.copyProperties(dto, user, "password", "id", "createTime");
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        this.removeById(id);
    }

    @Override
    public void resetPassword(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setStudentNo(user.getStudentNo());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setAvatar(user.getAvatar());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());
        return vo;
    }
}
