package com.lawProject.SSL.domain.notification.model;

import com.lawProject.SSL.global.common.dao.BaseEntity;
import com.lawProject.SSL.domain.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@Table(name="notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private String answer;

//    @NotNull
//    private NotificationType notificationType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notification(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
