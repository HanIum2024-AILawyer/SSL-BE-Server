package com.lawProject.SSL.domain.user.repository;

import com.lawProject.SSL.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserId(String userId);

    User findByProviderId(String providerId);
//    Optional<User> findByRefreshToken(String refreshToken);
}
