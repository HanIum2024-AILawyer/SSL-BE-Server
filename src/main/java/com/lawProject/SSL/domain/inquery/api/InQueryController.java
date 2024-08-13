package com.lawProject.SSL.domain.inquery.api;

import com.lawProject.SSL.domain.inquery.service.InQueryService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lawProject.SSL.domain.inquery.dto.InQueryDto.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquery")
public class InQueryController {
    private final InQueryService inQueryService;

    /* Q&A 작성 */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> write(HttpServletRequest request,
                                                     @RequestBody @Valid InQueryWriteRequest inQueryWriteRequest) {
        inQueryService.write(request, inQueryWriteRequest);

        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /* Q&A 상세 페이지 */
    @GetMapping("/{inQueryId}")
    public ResponseEntity<ApiResponse<Object>> getInQueryDetail(@PathVariable("inQueryId") Long inQueryId) {
        InQueryDetailResponse inQueryDetail = inQueryService.getInQueryDetail(inQueryId);

        return ApiResponse.onSuccess(SuccessCode._OK, inQueryDetail);
    }

    /* 나의 답변 대기 중인 Q&A 목록 */
    @GetMapping("/my/pending")
    public ResponseEntity<ApiResponse<Object>> getMyPendingInquiries(HttpServletRequest request) {
        List<InQueryListResponse> myInQuery = inQueryService.getMyInQuery(request, false);

        return ApiResponse.onSuccess(SuccessCode._OK, myInQuery);
    }

    /* 나의 답변 된 Q&A 목록 */
    @GetMapping("/my/answered")
    public ResponseEntity<ApiResponse<Object>> getMyAnsweredInquiries(HttpServletRequest request) {
        List<InQueryListResponse> myInQuery = inQueryService.getMyInQuery(request, true);

        return ApiResponse.onSuccess(SuccessCode._OK, myInQuery);
    }
}
