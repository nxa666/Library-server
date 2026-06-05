package com.library.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * JWT工具类（基于JDK原生实现，无需外部依赖）
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    /**
     * 生成token
     */
    public String generateToken(String username, Long userId) {
        long expireTime = System.currentTimeMillis() + expiration;
        String payload = username + "|" + userId + "|" + expireTime;
        String signature = sign(payload);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8)) + "." +
                Base64.getUrlEncoder().withoutPadding()
                        .encodeToString(signature.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getPayload(token).split("\\|")[0];
    }

    /**
     * 从token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getPayload(token).split("\\|")[1]);
    }

    /**
     * 校验token是否有效
     */
    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 2) return false;
            String payload = new String(
                    Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            String signature = new String(
                    Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return signature.equals(sign(payload))
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取token过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        long expireTime = Long.parseLong(getPayload(token).split("\\|")[2]);
        return new Date(expireTime);
    }

    /**
     * 判断token是否已过期
     */
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    private String getPayload(String token) {
        String[] parts = token.split("\\.");
        return new String(
                Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(keySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("JWT签名失败", e);
        }
    }
}
