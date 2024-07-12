package com.lawProject.SSL.domain.lawsuit.api;

import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.service.LawSuitService;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

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

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try{
            Resource resource = lawSuitService.downloadFile(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (MalformedURLException e) {
            log.info("File download failed", e);
            throw new FileException(ErrorCode.FILE_NOT_FOUND);
        }
    }
}
