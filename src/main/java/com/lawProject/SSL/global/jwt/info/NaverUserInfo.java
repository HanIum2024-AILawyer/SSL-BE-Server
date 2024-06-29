package com.lawProject.SSL.global.jwt.info;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
