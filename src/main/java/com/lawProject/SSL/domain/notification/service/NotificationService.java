package com.lawProject.SSL.domain.notification.service;

import com.lawProject.SSL.domain.notification.dao.NotificationRepository;
import com.lawProject.SSL.domain.notification.exception.NotificationException;
import com.lawProject.SSL.domain.notification.model.Notification;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.lawProject.SSL.domain.notification.dto.NotificationDto.NotificationDetailResponse;
import static com.lawProject.SSL.domain.notification.dto.NotificationDto.NotificationWriteRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;
    private UserService userService;

    /* Q&A 작성 메서드 */
    public void write(HttpServletRequest request, NotificationWriteRequest notificationWriteRequest) {
        User user = userService.getUserInfo(request);
        Notification notification = notificationWriteRequest.toEntity(user);
        notificationRepository.save(notification);
    }

    public NotificationDetailResponse getNotificationDetail(Long notificationId) {
        Notification notification = findNotificationById(notificationId);
        return NotificationDetailResponse.of(notification);
    }

    public Notification findNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }
}
