package com.lawProject.SSL.global.oauth.handler;

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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${jwt.redirect}")
    private String REDIRECT_URI; // 로그인 과정이 끝난 후 리다이렉트 되는 URI

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        // Access Token 발급
        String accessToken = jwtUtil.createAccessToken(user.getUserId());
        log.info("Access Token: {}", accessToken);
        // Refresh Token 발급 후 저장
        jwtUtil.createRefreshToken(user.getUserId(), accessToken);

        String redirectUrl = UriComponentsBuilder.fromUriString(REDIRECT_URI)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
        response.sendRedirect(redirectUrl);
    }
}
