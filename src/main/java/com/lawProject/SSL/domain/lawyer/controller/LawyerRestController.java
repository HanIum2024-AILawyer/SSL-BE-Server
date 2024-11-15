package com.lawProject.SSL.domain.lawyer.controller;

import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import com.lawProject.SSL.domain.lawyer.service.LawyerService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                                                            @RequestPart(name = "image") @Nullable MultipartFile image
                                                            ) throws IOException {
        lawyerService.create(request, image);

        return ApiResponse.onSuccess(SuccessCode._CREATED);
    }

    /* 변호사 목록 조회 */
    @GetMapping("/lawyers")
    public ResponseEntity<ApiResponse<Object>> listAllLawyers() {
        List<LawyerListResponse> lawyers = lawyerService.findLawyers();

        return ApiResponse.onSuccess(SuccessCode._OK, lawyers);
    }

    /* 변호사 단일 조회 */
    @GetMapping("/lawyers/{lawyerId}")
    public ResponseEntity<ApiResponse<Object>> getLawyerById(@PathVariable Long lawyerId) {
        Lawyer lawyer = lawyerService.findById(lawyerId);

        LawyerDetailResponse lawyerDetailResponse = LawyerDetailResponse.of(lawyer);

        return ApiResponse.onSuccess(SuccessCode._OK, lawyerDetailResponse);
    }

    /* 변호사 정보 수정 - 정보 가져오기 */
    @GetMapping("/admin/lawyers/{lawyerId}")
    public ResponseEntity<ApiResponse<Object>> getModifyLawyerInfo(
            @PathVariable Long lawyerId) {

        LawyerDetailResponse modifyLawyerInfo = lawyerService.getModifyLawyerInfo(lawyerId);

        return ApiResponse.onSuccess(SuccessCode._OK, modifyLawyerInfo);
    }

    /* 변호사 정보 수정 */
    @PutMapping("/admin/lawyers/{lawyerId}")
    public ResponseEntity<ApiResponse<Object>> ModifyLawyer(
            @PathVariable Long lawyerId,
            @RequestPart(name = "request") LawyerCreateRequest request,
            @RequestPart(name = "image", required = false) @Nullable MultipartFile image) {

        lawyerService.modifyLawyer(lawyerId, request, image);

        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /* 변호사 삭제, Admin */
    @DeleteMapping("/admin/lawyers/{lawyerId}")
    public ResponseEntity<ApiResponse<Object>> deleteLawyer(
            @PathVariable Long lawyerId
    ) {
        lawyerService.delete(lawyerId);

        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /*변호사 검색 */
    @GetMapping("/lawyers/search")
    public ResponseEntity<ApiResponse<List<LawyerListResponse>>> searchLawyer(
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<LawyerListResponse> searchList = lawyerService.search(keyword);
        return ApiResponse.onSuccess(SuccessCode._OK, searchList);
    }
}