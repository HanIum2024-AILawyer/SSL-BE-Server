package com.lawProject.SSL.global.oauth.info;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;
    // Test
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
