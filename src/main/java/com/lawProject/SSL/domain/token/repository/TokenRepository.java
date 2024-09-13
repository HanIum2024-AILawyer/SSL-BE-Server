package com.lawProject.SSL.domain.token.repository;

import com.lawProject.SSL.domain.token.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT u FROM Token u WHERE u.username = :username")
    Optional<Token> findByUsername(String username);

    Optional<Token> findByAccessToken(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token u WHERE u.username = :username")
    void deleteByUsername(String username);
}
