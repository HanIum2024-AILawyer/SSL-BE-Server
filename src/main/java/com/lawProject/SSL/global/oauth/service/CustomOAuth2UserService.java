package com.lawProject.SSL.global.oauth.service;

import com.lawProject.SSL.domain.token.repository.RefreshTokenRepository;
import com.lawProject.SSL.domain.user.repository.UserRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.oauth.info.GoogleUserInfo;
import com.lawProject.SSL.global.oauth.info.KaKaoUserInfo;
import com.lawProject.SSL.global.oauth.info.NaverUserInfo;
import com.lawProject.SSL.global.oauth.info.OAuth2UserInfo;
import com.lawProject.SSL.global.oauth.model.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 서비스 id (google, kakao, naver)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(provider, oAuth2User.getAttributes());

        User existUser = userRepository.findByProviderId(oAuth2UserInfo.getProviderId());
        User user;
        if (existUser != null) {
            user = existUser;
            log.info("기존 유저입니다.");
            refreshTokenRepository.deleteByUserId(user.getUserId());
        } else {
            log.info("신규 유저입니다. 등록을 진행합니다.");
            user  = createUser(oAuth2UserInfo);
            userRepository.save(user);
        }
//                .orElseGet(() -> createUser(oAuth2UserInfo));

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private User createUser(OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .name(oAuth2UserInfo.getName())
                .provider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .build();
    }

    private OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> new GoogleUserInfo(attributes);
            case "kakao" -> new KaKaoUserInfo(attributes);
            case "naver" -> new NaverUserInfo((Map<String, Object>) attributes.get("response"));
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
}
