package com.lawProject.SSL.domain.token.service;

import com.lawProject.SSL.domain.token.exception.TokenException;
import com.lawProject.SSL.domain.token.model.RefreshToken;
import com.lawProject.SSL.domain.token.model.TokenResponse;
import com.lawProject.SSL.domain.token.repository.RefreshTokenRepository;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public TokenResponse reissueAccessToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.extractRefreshToken(request).orElseThrow(() -> new TokenException(ErrorCode.INVALID_REFRESH_TOKEN));
        String userId = jwtUtil.extractUserId(refreshToken).orElseThrow(() -> new TokenException(ErrorCode.INVALID_REFRESH_TOKEN));
        RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(UUID.fromString(userId));
        String accessToken = null;

        if (!existRefreshToken.getToken().equals(refreshToken) || !jwtUtil.isTokenValid(refreshToken)) {
            log.info("Refresh Token이 일치하지 않거나, 만료되었습니다.");
            throw new TokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        } else {
            accessToken = jwtUtil.createAccessToken(UUID.fromString(userId));
        }

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
