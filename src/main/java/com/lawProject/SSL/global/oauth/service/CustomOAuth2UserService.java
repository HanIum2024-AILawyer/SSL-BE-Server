package com.lawProject.SSL.global.oauth.service;

import com.lawProject.SSL.domain.token.repository.TokenRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.domain.user.repository.UserRepository;
import com.lawProject.SSL.global.oauth.dto.UserDTO;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 서비스 id (google, kakao, naver)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        String username = oAuth2UserInfo.getProvider() + " " + oAuth2UserInfo.getProviderId();

        Optional<User> existUser = userRepository.findByUsername(username);
        User user;
        if (existUser.isEmpty()) {
            log.info("신규 유저입니다. 등록을 진행합니다.");
            user  = createUser(username, oAuth2UserInfo);
            userRepository.save(user);

            UserDTO userDTO = new UserDTO(username, oAuth2UserInfo.getName(), "USER");

            return new CustomOAuth2User(userDTO);
        } else {
            log.info("기존 유저입니다.");
            user = existUser.get();
            userRepository.save(user);

            tokenRepository.deleteByUsername(user.getUsername());
            UserDTO userDTO = new UserDTO(user.getUsername(), oAuth2UserInfo.getName(), user.getRole().toString());

            return new CustomOAuth2User(userDTO);
        }
    }

    /* User 생성 메서드 */
    private User createUser(String username, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .username(username)
                .name(oAuth2UserInfo.getName())
                .build();
    }

    /* 일치하는 provider를 찾고 OAuth2Info 생성 */
    private OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> new GoogleUserInfo(attributes);
            case "kakao" -> new KaKaoUserInfo(attributes);
            case "naver" -> new NaverUserInfo(attributes);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
}
