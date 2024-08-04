package com.lawProject.SSL.domain.notification.service;

import com.lawProject.SSL.domain.notification.dao.NotificationRepository;
import com.lawProject.SSL.domain.notification.model.Notification;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.lawProject.SSL.domain.notification.dto.NotificationDto.NotificationWriteRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;
    private UserService userService;

    public void write(HttpServletRequest request, NotificationWriteRequest notificationWriteRequest) {
        User user = userService.getUserInfo(request);
        Notification notification = notificationWriteRequest.toEntity(user);
        notificationRepository.save(notification);
    }
}
