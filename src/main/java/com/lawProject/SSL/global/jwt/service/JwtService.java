package com.lawProject.SSL.global.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.UUID;

public interface JwtService {
    String createAccessToken(UUID userId); // AccessToken 생성
    String createRefreshToken(UUID userId); // RefreshToken 생성 - 보안, I/O 감소 이유로 사용

//    void updateRefreshToken(UUID userId, String refreshToken); // RefreshToken 갱신
//
//    void destroyRefreshToken(UUID userId); // RefreshToken 삭제

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken); // Token들 전송
    void sendAccessToken(HttpServletResponse response, String accessToken); // AccessToken 전송

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    Optional<String> extractUserId(String accessToken);
    Optional<String> extractRefreshTokenUserId(String refreshToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);

    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    boolean isTokenValid(String token);

    // 응답 헤더에서 액세스 토큰을 반환하는 메서드
    public String getTokenFromHeader(String authorizationHeader);
}