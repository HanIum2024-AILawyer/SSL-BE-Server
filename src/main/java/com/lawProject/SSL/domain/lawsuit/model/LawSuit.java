package com.lawProject.SSL.domain.lawsuit.model;

import com.lawProject.SSL.domain.lawsuit.dto.FileStorageResult;
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

    private String storedFileName;

    private String originalFileName;

    private LocalDateTime expireTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /* Using Method */
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public void setOriginalFileName(String newOriginalFileName) {
        this.originalFileName = newOriginalFileName;
    }

    /* 연관관계 메서드 */
    public static LawSuit ofUser(User user, FileStorageResult fileStorageResult) {
        return LawSuit.builder()
                .user(user)
                .storedFileName(fileStorageResult.getStoredFileName())
                .originalFileName(fileStorageResult.getOriginalFileName())
                .build();
    }
}
