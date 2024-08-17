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
@RequestMapping("/api/lawyers")
public class LawyerRestController {

    private final LawyerService lawyerService;

    // Create a new lawyer, 어드민 권한 부여 필요
    @PostMapping
    public ResponseEntity<String> createLawyer(@Valid @RequestBody LawyerDto.LawyerForm form) {
        lawyerService.saveLawyer(form);

        return ResponseEntity.ok("success");
    }

    // List all lawyers
    @GetMapping
    public ResponseEntity<List<LawyerDto.LawyerListResponse>> listAllLawyers() {
        List<LawyerDto.LawyerListResponse> lawyers = lawyerService.findLawyers();
        return ResponseEntity.ok(lawyers);
    }

    // Get a specific lawyer
    @GetMapping("/{lawyerId}")
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

    // Update a lawyer
    @PutMapping("/{lawyerId}")
    public ResponseEntity<String> updateLawyer(
            @PathVariable Long lawyerId,
            @Valid @RequestBody LawyerDto.LawyerForm form) {

        lawyerService.updateLawyer(lawyerId, form);

        return ResponseEntity.ok("success");
    }

    // List all lawyers for admin
    @GetMapping("/admin")
    public ResponseEntity<List<LawyerDto.LawyerListResponse>> listAllLawyersForAdmin() {
        List<LawyerDto.LawyerListResponse> lawyers = lawyerService.findLawyers();
        return ResponseEntity.ok(lawyers);
    }

//    @GetMapping("/find") 변호사 검색 시스템
//    public ResponseEntity<String> findLawyer(){
//
//    }
//    @GetMapping() 태그 검색
//    public  ResponseEntity<> findTag(){
//
//    }
}
 //기본코드
//서비스 추가 필요