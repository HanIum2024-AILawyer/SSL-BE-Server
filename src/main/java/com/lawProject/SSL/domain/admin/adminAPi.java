package com.lawProject.SSL.domain.admin;

import com.lawProject.SSL.domain.inquery.dto.InQueryDto;
import com.lawProject.SSL.domain.inquery.service.InQueryService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class adminAPi {
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
}
