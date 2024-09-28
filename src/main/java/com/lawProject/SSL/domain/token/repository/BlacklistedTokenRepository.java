package com.lawProject.SSL.domain.token.repository;

import com.lawProject.SSL.domain.token.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {
    boolean existsByToken(String token);
    void deleteByCreatedAt(@Param("date") final LocalDateTime date);
}
