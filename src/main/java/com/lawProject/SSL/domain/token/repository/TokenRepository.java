package com.lawProject.SSL.domain.token.repository;

import com.lawProject.SSL.domain.token.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT u FROM Token u WHERE u.userId = :userId")
    Optional<Token> findByUserId(String userId);

    Optional<Token> findByAccessToken(String userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token u WHERE u.userId = :userId")
    void deleteByUserId(String userId);
}
