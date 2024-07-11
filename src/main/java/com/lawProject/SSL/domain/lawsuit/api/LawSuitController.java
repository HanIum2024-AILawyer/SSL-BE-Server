package com.lawProject.SSL.domain.lawsuit.api;

import com.lawProject.SSL.domain.lawsuit.service.LawSuitService;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lawsuit")
public class LawSuitController {
    private final LawSuitService lawSuitService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Object>> uploadFile(@RequestPart(name = "file") MultipartFile file, HttpServletRequest request) {
        try {
            String fileUrl = lawSuitService.uploadFile(file, request);
            return ApiResponse.onSuccess(SuccessCode._OK, fileUrl);
        } catch (IOException e) {
            log.info("File upload failed", e);
            return ApiResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR);
        }
    }
}
