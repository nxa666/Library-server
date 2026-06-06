package com.library.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.common.result.Result;
import com.library.dto.UserDTO;
import com.library.service.UserService;
import com.library.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "分页查询用户列表", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public Result<Page<UserVO>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String username) {
        Page<UserVO> page = userService.pageUsers(current, size, username);
        return Result.success(page);
    }

    @Operation(summary = "查询单个用户", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO vo = userService.getUserById(id);
        return Result.success(vo);
    }

    @Operation(summary = "新增用户", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public Result<String> add(@RequestBody UserDTO dto) {
        userService.addUser(dto);
        return Result.success("新增成功");
    }

    @Operation(summary = "编辑用户", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        userService.updateUser(id, dto);
        return Result.success("编辑成功");
    }

    @Operation(summary = "删除用户", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "重置密码", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}/reset-password")
    public Result<String> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success("密码已重置为123456");
    }
}
