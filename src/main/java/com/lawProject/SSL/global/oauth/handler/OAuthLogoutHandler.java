package com.lawProject.SSL.global.oauth.handler;

import com.lawProject.SSL.domain.token.exception.TokenException;
import com.lawProject.SSL.domain.token.model.BlacklistedToken;
import com.lawProject.SSL.domain.token.repository.BlacklistedTokenRepository;
import com.lawProject.SSL.domain.token.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = jwtUtil.extractAccessToken(request).orElseThrow(() -> new TokenException(ErrorCode.ACCESS_TOKEN_NOT_FOUND));
        if (!blacklistedTokenRepository.existsByToken(accessToken)) {
            blacklistedTokenRepository.save(new BlacklistedToken(accessToken));
        }

        String username = jwtUtil.extractUsername(accessToken);
        tokenRepository.deleteByUsername(username);

        log.info("User logged out and token invalidated");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
