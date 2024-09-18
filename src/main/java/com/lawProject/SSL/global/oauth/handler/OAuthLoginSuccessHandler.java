package com.lawProject.SSL.global.oauth.handler;

import com.lawProject.SSL.global.oauth.model.CustomOAuth2User;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.redirect}")
    private String REDIRECT_URI; // 로그인 과정이 끝난 후 리다이렉트 되는 URI
    @Value("${jwt.access-token.header}")
    private String AccessTokenHeader;
    @Value("${jwt.domain}")
    private String DOMAIN;

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // Access Token 발급
        String accessToken = jwtUtil.createAccessToken(username, role);
        log.info("Access Token: {}", accessToken);
        // Refresh Token 발급 후 저장
        jwtUtil.createRefreshToken(username, role, accessToken);

        response.addHeader("Set-Cookie" ,createCookie(AccessTokenHeader, accessToken));
        response.sendRedirect(REDIRECT_URI);
    }

    /* 프론트엔드 서버로 JWT를 전달할 때 Cookie 방식 사용 */
    private String createCookie(String key, String value) {

        ResponseCookie cookie = ResponseCookie.from(key, value)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .domain(DOMAIN)
                .secure(true)
                .build();

        return cookie.toString();
    }
}
