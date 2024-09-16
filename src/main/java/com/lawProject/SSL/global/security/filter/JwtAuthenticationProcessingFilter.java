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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * OncePerRequestFilter: 모든 서블릿 컨테이너에서 요청 디스패치당 단일 실행을 보장하는 것을 목표로 하는 필터 기본 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    private static final String NO_CHECK_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isExemptUrl(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> accessTokenOpt = jwtUtil.extractAccessToken(request);

        if (accessTokenOpt.isEmpty()) {
            proceedWithoutAuthentication(response, filterChain, request);
            return;
        }

        String accessToken = accessTokenOpt.get();

        if (!jwtUtil.isTokenValid(accessToken)) {
            handleExpiredAccessToken(request, response, filterChain, accessToken);
            return;
        }

        authenticateUser(accessToken);
        filterChain.doFilter(request, response);
    }

    // 특정 URL에 대한 토큰 체크 면제
    private boolean isExemptUrl(String requestURI) {
        return NO_CHECK_URL.equals(requestURI);
    }

    // AccessToken이 없는 경우
    private void proceedWithoutAuthentication(HttpServletResponse response, FilterChain filterChain, HttpServletRequest request) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        filterChain.doFilter(request, response);
    }

    // 만료된 AccessToken 처리
    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String accessToken) throws IOException, ServletException {
        Optional<Token> tokenOpt = tokenRepository.findByAccessToken(accessToken);

        if (tokenOpt.isEmpty() || !jwtUtil.isTokenValid(tokenOpt.get().getRefreshToken())) {
            setUnauthorized(response);
            filterChain.doFilter(request, response);
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
        response.addCookie(createCookie("Authorization", newAccessToken));
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

    // 쿠키 생성
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
}
