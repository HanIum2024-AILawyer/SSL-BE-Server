package com.lawProject.SSL.domain.user.application;

import com.lawProject.SSL.domain.user.dao.UserRepository;
import com.lawProject.SSL.domain.user.exception.UserException;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}
