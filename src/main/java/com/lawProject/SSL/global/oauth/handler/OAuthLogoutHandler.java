package com.lawProject.SSL.global.oauth.handler;

import com.lawProject.SSL.domain.token.exception.TokenException;
import com.lawProject.SSL.domain.token.model.BlacklistedToken;
import com.lawProject.SSL.domain.token.repository.BlacklistedTokenRepository;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLogoutHandler implements LogoutHandler, LogoutSuccessHandler {
    private final JwtUtil jwtUtil;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtUtil.extractAccessToken(request).orElseThrow(() -> new TokenException(ErrorCode.ACCESS_TOKEN_NOT_FOUND));
        if (!blacklistedTokenRepository.existsByToken(token)) {
            blacklistedTokenRepository.save(new BlacklistedToken(token));
        }
        log.info("User logged out and token invalidated");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
