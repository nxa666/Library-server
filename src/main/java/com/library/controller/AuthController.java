package com.library.controller;
import javax.servlet.http.HttpServletRequest;
import com.library.common.result.Result;
import com.library.dto.LoginDTO;
import com.library.dto.RegisterDTO;
import com.library.service.AuthService;
import com.library.vo.LoginVO;
import com.library.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 认证控制器
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        LoginVO vo = authService.login(dto);
        return Result.success("登录成功", vo);
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.success("注册成功");
    }

    @Operation(summary = "获取当前用户信息", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/info")
    public Result<UserInfoVO> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserInfoVO vo = authService.getCurrentUser(userId);
        return Result.success(vo);
    }
}
