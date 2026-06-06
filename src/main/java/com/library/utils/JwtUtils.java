package com.library.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * JWT工具类（基于JDK原生实现）
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private Long expire;

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    /**
     * 生成token
     */
    public String createToken(Long userId) {
        long expireTime = System.currentTimeMillis() + expire;
        String payload = userId + "|" + expireTime;
        String signature = sign(payload);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8)) + "." +
                Base64.getUrlEncoder().withoutPadding()
                        .encodeToString(signature.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从token中获取用户ID
     */
    public Long getUserId(String token) {
        return Long.parseLong(getPayload(token).split("\\|")[0]);
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
            String storedSignature = new String(
                    Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            String currentSignature = sign(payload);
            return storedSignature.equals(currentSignature) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断token是否已过期
     */
    public boolean isTokenExpired(String token) {
        long expireTime = Long.parseLong(getPayload(token).split("\\|")[1]);
        return expireTime < System.currentTimeMillis();
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
