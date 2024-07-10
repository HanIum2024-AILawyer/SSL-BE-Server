package com.lawProject.SSL.global.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawProject.SSL.domain.token.model.RefreshToken;
import com.lawProject.SSL.domain.token.repository.RefreshTokenRepository;
import com.lawProject.SSL.domain.user.dao.UserRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.oauth.model.CustomOAuth2User;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${jwt.redirect}")
    private String REDIRECT_URI; // 로그인 과정이 끝난 후 리다이렉트 되는 URI

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME; // 액세스 토큰 유효기간

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME; // 리프레쉬 토큰 유효기간

//    private final JwtUtil jwtUtil;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        // Refresh Token 발급 후 저장
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        // Access Token 발급
        String accessToken = jwtUtil.createAccessToken(user.getUserId());

        log.info("Refresh Token: {}", refreshToken);
        log.info("Access Token: {}", accessToken);

        jwtUtil.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        // 토큰 정보를 포함한 리다이렉트 URL 생성
        String redirectUrl = REDIRECT_URI + "?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
