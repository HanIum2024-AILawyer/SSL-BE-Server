package com.lawProject.SSL.domain.token.controller;

import com.lawProject.SSL.domain.token.model.TokenResponse;
import com.lawProject.SSL.domain.token.service.TokenService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    // Refresh Token이 유효할시 Access Token 재발급 API
    @GetMapping("/reissue/access-token")
    public ResponseEntity<ApiResponse<Object>> reissueAccessToken(HttpServletRequest request) {
        TokenResponse accessToken = tokenService.reissueAccessToken(request);
        return ApiResponse.onSuccess(SuccessCode.CREATED_ACCESS_TOKEN, accessToken);
    }
}
