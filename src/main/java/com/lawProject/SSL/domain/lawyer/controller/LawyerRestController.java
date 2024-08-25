package com.lawProject.SSL.domain.lawyer.controller;

import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import com.lawProject.SSL.domain.lawyer.dto.LawyerDto;
import com.lawProject.SSL.domain.lawyer.service.LawyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//RestController 사용

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LawyerRestController {

    private final LawyerService lawyerService;

    /* 변호사 등록, Admin */
    @PostMapping("/admin/lawyers")
    public ResponseEntity<String> createLawyer(@Valid @RequestBody LawyerDto.LawyerForm form) {
        lawyerService.saveLawyer(form);

        return ResponseEntity.ok("success");
    }

    /* 변호사 목록 조회 */
    @GetMapping("/lawyers")
    public ResponseEntity<List<LawyerDto.LawyerListResponse>> listAllLawyers() {
        List<LawyerDto.LawyerListResponse> lawyers = lawyerService.findLawyers();
        return ResponseEntity.ok(lawyers);
    }

    /* 변호사 단일 조회 */
    @GetMapping("/lawyers/{lawyerId}")
    public ResponseEntity<LawyerDto.LawyerDetailResponse> getLawyerById(@PathVariable Long lawyerId) {
        Lawyer lawyer = lawyerService.findById(lawyerId);

        /*lawyerService.findById에서 null 값에 대한 예외 처리를 하기 때문에 불필요*/
//        if (lawyer == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        /* Lawyer 정보 반환 전 DTO로 변환 */
        LawyerDto.LawyerDetailResponse lawyerDetailResponse = LawyerDto.LawyerDetailResponse.of(lawyer);

        return ResponseEntity.ok(lawyerDetailResponse);
    }

    /* 변호사 정보 수정 */
    @PutMapping("/admin/lawyers/{lawyerId}")
    public ResponseEntity<String> updateLawyer(
            @PathVariable Long lawyerId,
            @Valid @RequestBody LawyerDto.LawyerForm form) {

        lawyerService.updateLawyer(lawyerId, form);

        return ResponseEntity.ok("success");
    }
}