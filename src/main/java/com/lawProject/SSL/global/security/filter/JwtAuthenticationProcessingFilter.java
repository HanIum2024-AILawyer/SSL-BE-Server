package com.lawProject.SSL.global.security.filter;

import com.lawProject.SSL.domain.user.repository.UserRepository;
import com.lawProject.SSL.global.oauth.dto.UserDTO;
import com.lawProject.SSL.global.oauth.model.CustomOAuth2User;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * OncePerRequestFilter: 모든 서블릿 컨테이너에서 요청 디스패치당 단일 실행을 보장하는 것을 목표로 하는 필터 기본 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    @Value("${jwt.access-token.header}")
    private String AUTHORIZATION;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private static final String NO_CHECK_URL = "/login";

    /**
     * 1. RefreshToken이 오는 경우 -> 유효하면 AccessToken 재발급후, 필터 진행 X
     * 2. RefreshToken은 없고 AccessToken만 있는 경우 -> 유저정보 저장 후 필터 진행, RefreshToken 재발급X
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("Received request URI: " + requestURI);
        if (requestURI.equals(NO_CHECK_URL) || request.getHeader(AUTHORIZATION).isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    /**
     * request 헤더에서 accessToken을 가져온 뒤 유효한지 검증한다.
     * 유효하다면 인증 객체를 생성하고 요청을 다음 필터로 보낸다.
     * 유효하지 않다면 accessToken을 재발급하고, 재발급 된 accessToken을 헤더에 실어 다음 필터로 보낸다.
     */
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessTokenOpt = jwtUtil.extractAccessToken(request);
            String accessToken = accessTokenOpt.orElse("");

            if (StringUtils.hasText(accessToken)) {
                if (jwtUtil.isTokenValid(accessToken)) { // 토큰이 유효하다면
                    String username = jwtUtil.extractUsername(accessToken);
                    String role = jwtUtil.extractRole(accessToken);
                    UserDTO userDTO = new UserDTO(username, role);

                    //UserDetails에 회원 정보 객체 담기
                    CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
                    saveAuthentication(customOAuth2User);
                } else { // 토큰이 만료되었다면
                    String reissueAccessToken = jwtUtil.reissueAccessToken(accessToken);

                    if (StringUtils.hasText(reissueAccessToken)) {
                        String username = jwtUtil.extractUsername(reissueAccessToken);
                        String role = jwtUtil.extractRole(reissueAccessToken);
                        UserDTO userDTO = new UserDTO(username, role);

                        //UserDetails에 회원 정보 객체 담기
                        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
                        saveAuthentication(customOAuth2User);

                        response.setHeader(AUTHORIZATION, reissueAccessToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 예외를 로깅하고, 적절한 응답을 설정합니다.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token error");
        }
    }

    private void saveAuthentication(CustomOAuth2User customOAuth2User) {

        // Spring Security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
