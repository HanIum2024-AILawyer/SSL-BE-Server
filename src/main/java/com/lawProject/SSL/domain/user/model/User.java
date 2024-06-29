package com.lawProject.SSL.domain.user.model;

import com.lawProject.SSL.domain.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.lawProject.SSL.domain.user.model.UserRole.*;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "users_uuid", columnDefinition = "BINARY(16)", unique = true)
    private UUID userId;

    @Column(name = "name", nullable = false, length = 5)
    private String name;

    @Column(name = "provider", nullable = false, length = 10)
    private String provider; // google, kakao, naver

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId; // 카카오에서 이메일을 받을 수 없기 때문에, 로그인 시 providerId를 추출하여 각 유저를 구분한다.

    @NotNull
    private UserRole role = USER;

//    private UserStatus userStatus;

}