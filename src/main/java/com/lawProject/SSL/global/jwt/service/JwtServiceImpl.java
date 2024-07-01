package com.lawProject.SSL.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawProject.SSL.domain.user.dao.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.lawProject.SSL.global.common.constant.ConstraintConstants.*;

@Service
@RequiredArgsConstructor
@Transactional
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtServiceImpl implements JwtService {

    /*jwt.yml에 설정된 값 가져오기*/
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token.expiration-time}")
    private long accessTokenValidityInSeconds;
    @Value("${jwt.refresh-token.expiration-time}")
    private long refreshTokenValidityInSeconds;
    @Value("${jwt.access-token.header}")
    private String accessHeader;
    @Value("${jwt.refresh-token.header}")
    private String refreshHeader;

    private final UserRepository userRepository;
    private ObjectMapper objectMapper;


    @Override
    public String createAccessToken(UUID userId) {
        return JWT.create() // JWT 생성 빌더를 초기화
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject를 설정한다. subject는 토큰의 목적, 주제를 나타냄.
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds)) // 만료 시간 설정
                .withClaim(USERUUID_CLAIM, userId.toString()) // 토큰에 UserId 정보를 클레임으로 추가
                .sign(Algorithm.HMAC512(secret)); // HMAC512 알고리즘을 사용하여, 토큰에 서명. 서명 키: secret 변수로 설정된 값
    }

    @Override
    public String createRefreshToken(UUID userId) {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds))
                .withClaim(USERUUID_CLAIM, userId.toString())
                .sign(Algorithm.HMAC512(secret));
        // RefreshToken의 목적은 액세스 토큰의 갱신이기 때문에 클레임 포함X
    }

//    @Override
//    public void updateRefreshToken(UUID userId, String refreshToken) {
//        userRepository.findByUserId(userId)
//                .ifPresentOrElse(
//                        user -> user.updateRefreshToken(refreshToken), // 값이 존재한다면 refreshToken 업데이트
//                        () -> new UserException(USER_INQUIRY_FAILED) // 존재하지 않으면 예외 팔생
//                );
//    }
//
//    @Override
//    public void destroyRefreshToken(UUID userId) {
//        userRepository.findByUserId(userId)
//                .ifPresentOrElse(
//                        User::destroyRefreshToken, // 값이 존재한다면 refreshToken 삭제
//                        () -> new UserException(USER_INQUIRY_FAILED) // 존재하지 않으면 예외 발생
//                );
//    }

    // HTTPServletResponse를 사용하여 클라이언트에게 AccessToken, RefreshToken을 전송하는 메서드
    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken); //AccessToken을 Response 헤더에 설정
        setRefreshTokenHeader(response, refreshToken); //RefreshToken을 Response 헤더에 설정

        Map<String, String> tokenMap = new HashMap<>(); //토큰을 저장할 HashMap 저장
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);
    }

    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    /*토큰 추출*/
    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader)).filter(
                accessToken -> accessToken.startsWith(BEARER) //토큰이 Bearer로 시작하는지 확인
        ).map(accessToken -> accessToken.substring(BEARER.length()).trim()); // Bearer 접두사 제거 후 공백 제거
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader)).filter(
                refreshToken -> refreshToken.startsWith(BEARER)
        ).map(refreshToken -> refreshToken.substring(BEARER.length()).trim()); // Bearer 접두사 제거 후 공백 제거
    }

    @Override
    public Optional<String> extractUserId(String accessToken) {
        try {
            return Optional.ofNullable(
                    JWT.require(Algorithm.HMAC512(secret)).build().verify(accessToken).getClaim(USERUUID_CLAIM)
                            .asString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> extractRefreshTokenUserId(String refreshToken) {
        try {
            return Optional.ofNullable(
                    JWT.require(Algorithm.HMAC512(secret)).build().verify(refreshToken).getClaim(USERUUID_CLAIM)
                            .asString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 Token입니다.", e.getMessage());
            return false;
        }
    }

    // 응답 헤더에서 액세스 토큰을 반환하는 메서드
    public String getTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }
}