package com.lawProject.SSL.domain.admin;

import com.lawProject.SSL.domain.inquery.dto.InQueryDto;
import com.lawProject.SSL.domain.inquery.service.InQueryService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class adminApi {
    private final InQueryService inQueryService;

    /* Q&A 답글 달기 */
    @PostMapping("/inquery")
    public ResponseEntity<ApiResponse<Object>> answer(
            @RequestBody @Valid InQueryDto.InQueryAnswerRequest inQueryAnswerRequest,
            HttpServletRequest request
    ) {
        inQueryService.answer(request, inQueryAnswerRequest);

        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /* 답변 대기중인 Q&A 목록 조회 */
    @GetMapping("/inquery/pending")
    public ResponseEntity<ApiResponse<Object>> getMyPendingInquiries(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageImpl<InQueryDto.AdminInQueryListResponse> inQueryListResponse = inQueryService.getInQueryList(page, size, false);

        return ApiResponse.onSuccess(SuccessCode._OK, inQueryListResponse);
    }
    /* 답변된 Q&A 목록 조회 */
    @GetMapping("/inquery/answered")
    public ResponseEntity<ApiResponse<Object>> getMyAnsweredInquiries(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageImpl<InQueryDto.AdminInQueryListResponse> inQueryListResponse = inQueryService.getInQueryList(page, size, true);

        return ApiResponse.onSuccess(SuccessCode._OK, inQueryListResponse);
    }
}
