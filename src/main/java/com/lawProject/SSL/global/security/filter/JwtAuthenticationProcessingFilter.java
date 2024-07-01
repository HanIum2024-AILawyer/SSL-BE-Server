package com.lawProject.SSL.global.security.filter;

import com.lawProject.SSL.domain.user.dao.UserRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.jwt.repository.RefreshTokenRepository;
import com.lawProject.SSL.global.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * OncePerRequestFilter: 모든 서블릿 컨테이너에서 요청 디스패치당 단일 실행을 보장하는 것을 목표로 하는 필터 기본 클래스
 */
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private static final String NO_CHECK_URL = "/login";

    /**
     * 1. RefreshToken이 오는 경우 -> 유효하면 AccessToken 재발급후, 필터 진행 X
     * 2. RefreshToken은 없고 AccessToken만 있는 경우 -> 유저정보 저장 후 필터 진행, RefreshToken 재발급X
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Received request URI: " + request.getRequestURI());
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) { // Request 요청으로 온 사용자가 refreshToken을 가지고 있다면 AccessToken을 재발급
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(

                accessToken -> jwtService.extractUserId(accessToken).ifPresent(

                        userId -> userRepository.findByUserId(UUID.fromString(userId)).ifPresent(
                                user -> saveAuthentication(user)
                        )
                )
        );

        filterChain.doFilter(request,response);
    }



    private void saveAuthentication(User users) {
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(users.getName())
                .roles(users.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authoritiesMapper.mapAuthorities(user.getAuthorities()));


        SecurityContext context = SecurityContextHolder.createEmptyContext();//5
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
//        userRepository.findByRefreshToken(refreshToken).ifPresent(
//                user -> jwtService.sendAccessToken(response, jwtService.createAccessToken(user.getUserId()))
//        );

        jwtService.extractRefreshTokenUserId(refreshToken).ifPresent(
                userId -> userRepository.findByUserId(UUID.fromString(userId)).ifPresent(
                        user -> jwtService.sendAccessToken(response, jwtService.createAccessToken(user.getUserId()))
                )
        );


    }
}
