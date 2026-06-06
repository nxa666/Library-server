package com.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.dto.UserDTO;
import com.library.entity.User;
import com.library.vo.UserVO;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    Page<UserVO> pageUsers(Long current, Long size, String username);

    UserVO getUserById(Long id);

    void addUser(UserDTO dto);

    void updateUser(Long id, UserDTO dto);

    void deleteUser(Long id);

    void resetPassword(Long id);
}
