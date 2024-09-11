package com.lawProject.SSL.global.util;

import com.lawProject.SSL.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface JwtUtil {
    String createAccessToken(String userId); // AccessToken 생성
    void createRefreshToken(String userId, String accessToken); // RefreshToken 생성 - 보안, I/O 감소 이유로 사용

//    void updateRefreshToken(UUID userId, String refreshToken); // RefreshToken 갱신
//
//    void destroyRefreshToken(UUID userId); // RefreshToken 삭제

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken); // Token들 전송
    void sendAccessToken(HttpServletResponse response, String accessToken); // AccessToken 전송

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    String extractUserId(String accessToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);

    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    boolean isTokenValid(String token);

    User getUserFromRequest(HttpServletRequest request);

    String reissueAccessToken(String accessToken);
}