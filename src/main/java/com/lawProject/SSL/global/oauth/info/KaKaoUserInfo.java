package com.lawProject.SSL.global.oauth.info;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class KaKaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        // kakao_account라는 Map에서 추출
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }
}
