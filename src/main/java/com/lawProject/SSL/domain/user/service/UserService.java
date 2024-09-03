package com.lawProject.SSL.domain.user.service;

import com.lawProject.SSL.domain.user.repository.UserRepository;
import com.lawProject.SSL.domain.user.exception.UserException;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public User getUserInfo(HttpServletRequest request) {
        return jwtUtil.getUserFromRequest(request);
    }
}
