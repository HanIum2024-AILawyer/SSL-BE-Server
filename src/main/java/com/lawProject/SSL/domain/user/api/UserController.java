//package com.lawProject.SSL.domain.user.api;
//
//
//import com.lawProject.SSL.domain.user.application.UserKakaoService;
//import com.lawProject.SSL.global.common.response.ApiResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class UserController {
//    private final UserKakaoService userKakaoService;
//
//    @PostMapping("/loginKakao")
//    public ResponseEntity<ApiResponse<Object>> kakaoLogin(@RequestParam String code, HttpServletRequest response) {
//        String createToken userKakaoService.kakaoLogin(code, response);
//    }
//}
