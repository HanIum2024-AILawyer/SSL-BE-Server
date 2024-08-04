package com.lawProject.SSL.domain.notification.api;

import com.lawProject.SSL.domain.notification.service.NotificationService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lawProject.SSL.domain.notification.dto.NotificationDto.NotificationWriteRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> write(HttpServletRequest request,
                                                     @RequestBody @Valid NotificationWriteRequest notificationWriteRequest) {
        notificationService.write(request, notificationWriteRequest);

        return ApiResponse.onSuccess(SuccessCode._OK);
    }
}
