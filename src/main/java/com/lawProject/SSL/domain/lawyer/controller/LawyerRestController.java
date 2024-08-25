package com.lawProject.SSL.domain.lawyer.controller;

import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import com.lawProject.SSL.domain.lawyer.service.LawyerService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.lawProject.SSL.domain.lawyer.dto.LawyerDto.*;


//RestController 사용

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LawyerRestController {

    private final LawyerService lawyerService;

    /* 변호사 등록, Admin */
    @PostMapping("/admin/lawyers")
    public ResponseEntity<ApiResponse<Object>> createLawyer(@RequestPart(name = "request") LawyerCreateRequest request,
                                                            @RequestPart(name = "image") MultipartFile image
                                                            ) {
        lawyerService.create(request, image);

        return ApiResponse.onSuccess(SuccessCode._CREATED);
    }

    /* 변호사 목록 조회 */
    @GetMapping("/lawyers")
    public ResponseEntity<List<LawyerListResponse>> listAllLawyers() {
        List<LawyerListResponse> lawyers = lawyerService.findLawyers();
        return ResponseEntity.ok(lawyers);
    }

    /* 변호사 단일 조회 */
    @GetMapping("/lawyers/{lawyerId}")
    public ResponseEntity<LawyerDetailResponse> getLawyerById(@PathVariable Long lawyerId) {
        Lawyer lawyer = lawyerService.findById(lawyerId);

        /*lawyerService.findById에서 null 값에 대한 예외 처리를 하기 때문에 불필요*/
//        if (lawyer == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        /* Lawyer 정보 반환 전 DTO로 변환 */
        LawyerDetailResponse lawyerDetailResponse = LawyerDetailResponse.of(lawyer);

        return ResponseEntity.ok(lawyerDetailResponse);
    }

    /* 변호사 정보 수정 */
    @PutMapping("/admin/lawyers/{lawyerId}")
    public ResponseEntity<String> updateLawyer(
            @PathVariable Long lawyerId,
            @RequestPart(name = "request") LawyerCreateRequest request,
            @RequestPart(name = "image") MultipartFile image) {

        lawyerService.updateLawyer(lawyerId, request, image);

        return ResponseEntity.ok("success");
    }
}