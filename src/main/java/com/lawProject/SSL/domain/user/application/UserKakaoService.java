//package com.lawProject.SSL.domain.user.application;
//
//import com.lawProject.SSL.domain.user.dao.UserRepository;
//import com.lawProject.SSL.global.util.JwtUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserKakaoService {
//
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final PasswordEncoder passwordEncoder;
//    public String kakaoLogin(String code, HttpServletRequest response) {
//        LoginKakaoRequestDto kakaoUserInfo = getKakaoUserInfo(code);
//
//        User kakaoUser = registerKakaoUser(kakaoUserInfo);
//
//        String accessToken = jwtUtil.createAccessToken(kakaoUser.getUserId());
//        String refreshToken = jwtUtil.createRefreshToken()
//        jwtUtil.sendAccessAndRefreshToken();
//
//        return accessToken;
//    }
//}
