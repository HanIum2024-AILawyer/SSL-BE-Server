package com.lawProject.SSL.domain.notification.api;

import com.lawProject.SSL.domain.notification.service.NotificationService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lawProject.SSL.domain.notification.dto.NotificationDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private NotificationService notificationService;

    /* Q&A 작성 */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> write(HttpServletRequest request,
                                                     @RequestBody @Valid NotificationWriteRequest notificationWriteRequest) {
        notificationService.write(request, notificationWriteRequest);

        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /* Q&A 상세 페이지 */
    @GetMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Object>> getNotificationDetail(@PathVariable("notificationId") Long notificationId) {
        NotificationDetailResponse notificationDetail = notificationService.getNotificationDetail(notificationId);

        return ApiResponse.onSuccess(SuccessCode._OK, notificationDetail);
    }

    /* Q&A 목록 조회 */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getNotificationList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageImpl<NotificationListResponse> notificationListResponse = notificationService.getNotificationList(page, size);

        return ApiResponse.onSuccess(SuccessCode._OK, notificationListResponse);
    }

    /* 나의 Q&A */

    /* Q&A 답글 달기 */
}
