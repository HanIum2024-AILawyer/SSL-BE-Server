package com.lawProject.SSL.domain.notification.dto;

import com.lawProject.SSL.domain.notification.model.Notification;
import com.lawProject.SSL.domain.user.model.User;
import jakarta.validation.constraints.NotNull;

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
}

