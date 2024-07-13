package com.lawProject.SSL.domain.lawsuit.model;

import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.dao.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LawSuit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "law_suit_id")
    private Long id;

    private String fileUrl;

    private LocalDateTime expireTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /* Using Method */
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    /* 연관관계 메서드 */
    public static LawSuit ofUser(User user, String fileUrl) {
        return LawSuit.builder()
                .user(user)
                .fileUrl(fileUrl)
                .build();
    }
}
