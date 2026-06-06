package com.library.common.interceptor;

import com.library.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    // 完全放行的路径
    private static final String[] EXCLUDE_PATHS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/test",
            "/doc.html",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars",
            "/swagger-ui",
            "/favicon.ico",
            "/error"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Authorization = " + request.getHeader("Authorization"));
        String path = request.getRequestURI();

        // 检查是否在放行列表中
        for (String excludePath : EXCLUDE_PATHS) {
            if (path.startsWith(excludePath)) {
                return true;
            }
        }

        // 获取请求头中的token
        String header = request.getHeader(AUTH_HEADER);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            writeResponse(response, 401, "未登录或登录已过期");
            return false;
        }

        // 提取token（去掉 "Bearer " 前缀）
        String token = header.substring(TOKEN_PREFIX.length());

        // 校验token
        if (!jwtUtils.validateToken(token)) {
            writeResponse(response, 401, "Token无效或已过期");
            return false;
        }

        // 将用户ID存入请求属性，供后续使用
        Long userId = jwtUtils.getUserId(token);
        request.setAttribute("userId", userId);

        return true;
    }

    private void writeResponse(HttpServletResponse response, int code, String message) throws Exception {
        // 返回200状态码，避免浏览器弹出HTTP Basic Auth登录框
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        Result<String> result = Result.error(code, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
