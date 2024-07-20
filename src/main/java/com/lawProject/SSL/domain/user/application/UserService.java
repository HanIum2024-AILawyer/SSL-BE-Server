package com.lawProject.SSL.domain.user.application;

import com.lawProject.SSL.domain.user.dao.UserRepository;
import com.lawProject.SSL.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id) {

    }
}
