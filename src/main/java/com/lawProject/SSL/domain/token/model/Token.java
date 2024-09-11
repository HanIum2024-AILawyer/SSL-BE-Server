package com.lawProject.SSL.domain.token.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_tokens_id")
    private Long id;

    @Column(name = "users_uuid", columnDefinition = "VARCHAR(36)", unique = true)
    private String userId;

    @Column(name = "refreshToken", nullable = false)
    private String refreshToken;

    @Column(name = "accessToken", nullable = false)
    private String accessToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Token(String userId, String refreshToken, String accessToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
