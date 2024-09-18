package com.lawProject.SSL.domain.token.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken {
    @Id
    @Column(name = "token", nullable = false, length = 500)
    private String token;
}
