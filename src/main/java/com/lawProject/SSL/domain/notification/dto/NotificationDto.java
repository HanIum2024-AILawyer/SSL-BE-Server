package com.lawProject.SSL.domain.notification.dto;

import com.lawProject.SSL.domain.notification.model.Notification;
import com.lawProject.SSL.domain.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class NotificationDto {

    /**
     * Request
     */
    public record NotificationWriteRequest(
            @NotNull
            String title,
            @NotNull
            String content
    ) {
        public Notification toEntity(User user) {
            return Notification.builder()
                    .title(title)
                    .content(title)
                    .user(user)
                    .build();
        }
    }

    /**
     * Response
     */
    @Builder
    public record NotificationDetailResponse(
            String title,
            String content,
            String answer,
            String userName
    ) {
        public static NotificationDetailResponse of(Notification notification) {
            return NotificationDetailResponse.builder()
                    .title(notification.getTitle())
                    .content(notification.getContent())
                    .answer(notification.getAnswer())
                    .userName(notification.getUser().getName())
                    .build();
        }
    }

    @Builder
    public record NotificationListResponse(
            String title,
            String content,
            boolean isAnswer
    ) {
        public static NotificationListResponse of(Notification notification) {
            return NotificationListResponse.builder()
                    .title(notification.getTitle())
                    .content(notification.getContent())
                    .isAnswer(notification.isAnswer())
                    .build();
        }
    }

}

