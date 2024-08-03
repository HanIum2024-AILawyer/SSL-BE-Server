package com.lawProject.SSL.domain.token.repository;

import com.lawProject.SSL.domain.token.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {
    boolean existsByToken(String token);

}
