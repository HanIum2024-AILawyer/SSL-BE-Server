package com.lawProject.SSL.global.security.oauth.handler;

import com.lawProject.SSL.domain.user.dao.UserRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.jwt.service.JwtService;
import com.lawProject.SSL.global.security.oauth.info.GoogleUserInfo;
import com.lawProject.SSL.global.security.oauth.info.KaKaoUserInfo;
import com.lawProject.SSL.global.security.oauth.info.NaverUserInfo;
import com.lawProject.SSL.global.security.oauth.info.OAuth2UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${jwt.redirect}")
    private String REDIRECT_URI; // 프론트엔드로 Jwt 토큰을 리다이렉트할 URI

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME; // 액세스 토큰 유효기간

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME; // 리프레쉬 토큰 유효기간

    private OAuth2UserInfo oAuth2UserInfo = null;

//    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final UserRepository userRepository;
//    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        final String provider = token.getAuthorizedClientRegistrationId();// provider

        // 구글, 카카오, 네이버 로그인 요청
        switch (provider) {
            case "google" -> {
                log.info("구글 로그인 요청");
                oAuth2UserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());
            }
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                oAuth2UserInfo = new KaKaoUserInfo(token.getPrincipal().getAttributes());
            }
            case "naver" -> {
                log.info("네이버 로그인 요청");
                oAuth2UserInfo = new NaverUserInfo(token.getPrincipal().getAttributes());
            }
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 제공자입니다: " + provider);
        }

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();

        User existUser = userRepository.findByProviderId(providerId);
        User user;

        if(existUser == null) {
            // 신규 유저인 경우
            log.info("신규 유저입니다. 등록을 진행합니다.");

            user = User.builder()
                    .userId(UUID.randomUUID())
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else {
            // 기존 유저인 경우
            log.info("기존 유저입니다.");
//            refreshTokenRepository.deleteByUserId(existUser.getUserId());
            user = existUser;
        }

        String accessToken = jwtService.createAccessToken(user.getUserId());
        String refreshtoken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshtoken);

        user.updateRefreshToken(refreshtoken);

        log.info("유저 이름 : {}", name);
        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);

//        log.info("AccessToken을 발급합니다. AccessToken: {}", accessToken);
//        log.info("RefreshToken을 발급합니다. RefreshToken: {}", refreshtoken);

//        // 리프레쉬 토큰 발급 후 저장
//        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), REFRESH_TOKEN_EXPIRATION_TIME);
//        RefreshToken newRefreshToken = RefreshToken.builder()
//                .userId(user.getUserId())
//                .token(refreshToken)
//                .build();
//        refreshTokenRepository.save(newRefreshToken);
//
//        //액세스 토큰 발급
//        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), ACCESS_TOKEN_EXPIRATION_TIME);
//
//        //이름, 액세스 토큰, 리프레쉬 토큰을 담아 리다이렉트
//        String encodedName = URLEncoder.encode(name, "UTF-8");
//        String redirectUri = String.format(REDIRECT_URI, encodedName, accessToken, refreshToken);
//        getRedirectStrategy().sendRedirect(request, response, redirectUri);

    }
}
