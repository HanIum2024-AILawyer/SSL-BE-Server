package com.lawProject.SSL.global.util;

import com.lawProject.SSL.global.jwt.exception.TokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static com.lawProject.SSL.global.error.ErrorCode.INVALID_TOKEN;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /* Access Token 발급 */
    public String generateAccessToken(UUID userId, long expirationMillis) {
        log.info("액세스 토큰이 발급되었습니다.");

        return Jwts.builder()
                .claim("userId", userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(this.getSigningKey())
                .compact();
    }

    /* Refresh Token 발급 */
    public String generateRefreshToken(UUID userId, long expirationMillis) {
        log.info("리프레쉬 토큰이 발급되었습니다.");

        return Jwts.builder()
                .claim("userId", userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(this.getSigningKey())
                .compact();
    }

    /* 응답 헤더에서 액세스 토큰을 반환하는 메서드 */
    public String getTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    /* 토큰에서 userId를 반환하는 메서드 */
    public String getUserIdFromToken(String token) {
        try {
            String userId = Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseEncryptedClaims(token)
                    .getPayload()
                    .get("userId", String.class);
            log.info("userId 반환");
            return userId;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            log.warn("유효하지 않은 토큰입니다.");
            throw new TokenException(INVALID_TOKEN);
        }
    }

    /* JWT 토큰의 유효기간 확인 */
    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseEncryptedClaims(token)
                    .getPayload()
                    .getExpiration();
            log.info("토큰의 유효기간을 확인합니다.");
            return expirationDate.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            log.warn("유효하지 않은 토큰입니다.");
            throw new TokenException(INVALID_TOKEN);
        }
    }
}
