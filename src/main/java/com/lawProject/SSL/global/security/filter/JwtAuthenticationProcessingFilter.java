package com.lawProject.SSL.global.security.filter;

import com.lawProject.SSL.domain.token.model.Token;
import com.lawProject.SSL.domain.token.repository.TokenRepository;
import com.lawProject.SSL.global.oauth.dto.UserDTO;
import com.lawProject.SSL.global.oauth.model.CustomOAuth2User;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * OncePerRequestFilter: 모든 서블릿 컨테이너에서 요청 디스패치당 단일 실행을 보장하는 것을 목표로 하는 필터 기본 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    @Value("${jwt.access-token.header}")
    private String AccessTokenHeader;
    @Value("${jwt.domain}")
    private String DOMAIN;

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    private static final String NO_CHECK_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("request URl: {}", request.getRequestURI());
        if (isExemptUrl(request.getRequestURI())|| request.getRequestURI().equals("/")) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> accessTokenOpt = jwtUtil.extractAccessToken(request);

        if (accessTokenOpt.isEmpty()) {
            setUnauthorized(response);
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = accessTokenOpt.get();

        if (!jwtUtil.isTokenValid(accessToken)) {
            handleExpiredAccessToken(request, response, filterChain, accessToken);
            filterChain.doFilter(request, response);
        } else {
            authenticateUser(accessToken);
            filterChain.doFilter(request, response);
        }
    }

    // 특정 URL에 대한 토큰 체크 면제
    private boolean isExemptUrl(String requestURI) {
        List<String> exemptUrls = exemptUrls();
        boolean isExempt = exemptUrls.stream().anyMatch(url -> {
            boolean matches = requestURI.startsWith(url);
            return matches;
        });
        return isExempt;
    }

    // 만료된 AccessToken 처리
    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String accessToken) throws IOException, ServletException {
        Optional<Token> tokenOpt = tokenRepository.findByAccessToken(accessToken);

        if (tokenOpt.isEmpty() || !jwtUtil.isTokenValid(tokenOpt.get().getRefreshToken())) {
            setUnauthorized(response);
            return;
        }

        Token token = tokenOpt.get();
        String refreshToken = token.getRefreshToken();
        String newAccessToken = generateNewAccessToken(refreshToken);

        updateTokenAndAddCookie(response, token, newAccessToken);
        authenticateUser(newAccessToken);
    }

    // 새 AccessToken 생성
    private String generateNewAccessToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);
        return jwtUtil.createAccessToken(username, role);
    }

    // AccessToken 갱신 및 저장
    private void updateTokenAndAddCookie(HttpServletResponse response, Token token, String newAccessToken) {
        token.updateAccessToken(newAccessToken);
        tokenRepository.save(token);
        response.addCookie(createCookie(AccessTokenHeader, newAccessToken));
    }

    // 유효한 AccessToken일 경우 인증 처리
    private void authenticateUser(String accessToken) {
        String username = jwtUtil.extractUsername(accessToken);
        String role = jwtUtil.extractRole(accessToken);
        UserDTO userDTO = new UserDTO(username, role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        saveAuthentication(customOAuth2User);
    }

    // 스프링 시큐리티 인증 저장
    private void saveAuthentication(CustomOAuth2User customOAuth2User) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /* 프론트엔드 서버로 JWT를 전달할 때 Cookie 방식 사용 */
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    // 인증 실패 처리
    private void setUnauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private List<String> exemptUrls() {
        return Arrays.asList(
                "/css/**",         // CSS 파일들
                "/images/**",      // 이미지 파일들
                "/js/**",          // 자바스크립트 파일들
                "/favicon.*",      // 파비콘 파일
                "/*/icon-*",       // 아이콘 파일
                "/error",          // 에러 페이지
                "/error/**",       // 에러 관련 경로
                "/stomp/**",       // STOMP 관련 경로
                "/redis/**",       // Redis 관련 경로
                "/join",           // 회원가입 경로
                "/login",       // 로그인 관련 경로
                "/info",           // 정보 관련 경로
                "/info/**",        // 정보 관련 하위 경로
                "/api/v1/inquery/**",
                "/api/v1/lawyers",
                "/api/v1/lawyers/**",
                "/healthcheck"
        );
    }
}
