package com.lawProject.SSL.global.oauth.handler;

import com.lawProject.SSL.domain.user.dao.UserRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.domain.token.model.RefreshToken;
import com.lawProject.SSL.domain.token.repository.RefreshTokenRepository;
import com.lawProject.SSL.global.util.JwtUtil;
import com.lawProject.SSL.global.oauth.info.GoogleUserInfo;
import com.lawProject.SSL.global.oauth.info.KaKaoUserInfo;
import com.lawProject.SSL.global.oauth.info.NaverUserInfo;
import com.lawProject.SSL.global.oauth.info.OAuth2UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

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
    private final JwtUtil jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        final String provider = token.getAuthorizedClientRegistrationId();// provider

        // 구글, 카카오, 네이버 로그인 요청
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(provider, token);

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();

        User existUser = userRepository.findByProviderId(providerId);
        User user;

        // 기존 사용자라면 리프레시 토큰 제거
        if (existUser != null) {
            user = existUser;
            log.info("기존 유저입니다.");
            refreshTokenRepository.deleteByUserId(user.getUserId());
        } else {
            log.info("신규 유저입니다. 등록을 진행합니다.");
            user = User.builder()
                    .userId(UUID.randomUUID())
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(user);
        }

        log.info("유저 이름 : {}", name);
        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);

        // Refresh Token 발급 후 저장
        String refreshtoken = jwtService.createRefreshToken(user.getUserId());

        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshtoken)
                .build();

        refreshTokenRepository.save(newRefreshToken);

        // Access Token 발급
        String accessToken = jwtService.createAccessToken(user.getUserId());

        log.info("Refresh Token: {}", refreshtoken);
        log.info("Access Token: {}", accessToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshtoken);

        redirectStrategy.sendRedirect(request, response, "/");

    }

    private OAuth2UserInfo getOAuth2UserInfo(String provider, OAuth2AuthenticationToken token) {
        switch (provider) {
            case "google" -> {
                log.info("구글 로그인 요청");
                return new GoogleUserInfo(token.getPrincipal().getAttributes());
            }
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                return new KaKaoUserInfo(token.getPrincipal().getAttributes());
            }
            case "naver" -> {
                log.info("네이버 로그인 요청");
                return new NaverUserInfo(token.getPrincipal().getAttributes());
            }
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 제공자입니다: " + provider);
        }
    }
}
