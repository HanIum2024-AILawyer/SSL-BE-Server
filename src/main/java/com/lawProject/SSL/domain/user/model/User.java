package com.lawProject.SSL.domain.user.model;

import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import com.lawProject.SSL.domain.inquery.model.InQuery;
import com.lawProject.SSL.global.common.dao.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.lawProject.SSL.domain.user.model.UserRole.USER;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "tblUser")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "users_uuid", columnDefinition = "VARCHAR(36)", unique = true)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provider", nullable = false, length = 10)
    private String provider; // google, kakao, naver

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId; // 카카오에서 이메일을 받을 수 없기 때문에, 로그인 시 providerId를 추출하여 각 유저를 구분한다.

    @Enumerated(EnumType.STRING)
    private UserRole role = USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<LawSuit> lawSuitList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<InQuery> inQueryList = new ArrayList<>();

    @Builder
    public User(String userId, String name, String provider, String providerId) {
        this.userId = userId;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void addInQuery(InQuery inQuery) {
        this.inQueryList.add(inQuery);
    }

    public void addLawsuit(LawSuit lawSuit) {
        this.lawSuitList.add(lawSuit);
    }
}
