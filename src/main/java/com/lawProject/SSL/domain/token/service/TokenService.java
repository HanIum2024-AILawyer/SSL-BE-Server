package com.lawProject.SSL.domain.token.service;

import com.lawProject.SSL.domain.token.exception.TokenException;
import com.lawProject.SSL.domain.token.model.Token;
import com.lawProject.SSL.domain.token.repository.BlacklistedTokenRepository;
import com.lawProject.SSL.domain.token.repository.TokenRepository;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TokenService {
    private final TokenRepository tokenRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    /* 리프레쉬 토큰 업데이트 또는 토큰 생성 후 저장 */
    @Transactional
    public void saveOrUpdate(String username, String refreshToken, String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(t -> t.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(username, refreshToken, accessToken));

        tokenRepository.save(token);
    }

    /* accessToken과 일치하는 token 반환 */
    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.INVALID_ACCESS_TOKEN));
    }

    /* Access 토큰 정보 업데이트 */
    @Transactional
    public void updateAccessToken(String accessToken, Token token) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);

        log.info(token.getAccessToken());
    }

    /* 토큰이 존재하는지, 일치하는지, 유효한지 검증 */
    private void validateRefreshToken(String username, String refreshToken) {
        Optional<Token> existRefreshTokenOpt = tokenRepository.findByUsername(username);
        if (!existRefreshTokenOpt.isPresent()) {
            throw new TokenException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        if ((!existRefreshTokenOpt.get().getRefreshToken().equals(refreshToken))) {
            throw new TokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    /**
     * RefreshToken의 만료 기한(7일)이 지난 토큰은 자정에 자동 삭제
     * AccessToken의 만료 기한(1시간)이 지난 블랙리스트는 자정에 자동 삭제
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteToken() {
        LocalDateTime date7 = LocalDateTime.now().minusDays(7);
        tokenRepository.deleteByCreatedAt(date7);
//        LocalDateTime date1 = LocalDateTime.now().minusHours(1);
//        blacklistedTokenRepository.deleteByCreatedAt(date1);
    }
}
