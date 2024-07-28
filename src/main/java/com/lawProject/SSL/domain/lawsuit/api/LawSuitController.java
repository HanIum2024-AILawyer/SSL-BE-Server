package com.lawProject.SSL.domain.lawsuit.api;

import com.lawProject.SSL.domain.lawsuit.dto.FileStorageResult;
import com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto;
import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.service.FileService;
import com.lawProject.SSL.domain.lawsuit.service.LawSuitService;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.*;
import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.LawSuitResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lawsuit")
public class LawSuitController {
    private final LawSuitService lawSuitService;
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Object>> uploadFile(@RequestPart(name = "file") MultipartFile file, HttpServletRequest request) {
        try {
            // 파일 형식 검사
            if (!isWordFile(file)) {
                return ApiResponse.onFailure(ErrorCode.INVALID_FILE_TYPE);
            }
            FileStorageResult fileStorageResult = lawSuitService.uploadFile(file, request);
            return ApiResponse.onSuccess(SuccessCode._OK, fileStorageResult);
        } catch (IOException e) {
            log.info("File upload failed", e);
            return ApiResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{storedFileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String storedFileName) {
        try {
            Resource resource = fileService.loadFileAsResource(storedFileName);
            String originalFileName = lawSuitService.getOriginalFileName(storedFileName);

            String extension = getFileExtension(originalFileName);
            MediaType mediaType;

            if ("doc".equalsIgnoreCase(extension)) {
                mediaType = MediaType.parseMediaType("application/msword");
            } else if ("docx".equalsIgnoreCase(extension)) {
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            } else {
                mediaType = MediaType.APPLICATION_OCTET_STREAM; // 기본 MIME 타입
            }

            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            String contentDisposition = String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", encodedFileName, encodedFileName);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode file name", e);
            throw new FileException(ErrorCode.FILE_NOT_FOUND);
        } catch (FileException e) {
            log.info("File download failed", e);
            throw new FileException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getLawSuitList(HttpServletRequest request) {
        List<LawSuitResponse> lawSuitList = lawSuitService.getLawSuitList(request);
        return ApiResponse.onSuccess(SuccessCode._OK, lawSuitList);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Object>> changeOriginalFileName(HttpServletRequest request, @RequestBody UpdateFileNameLawSuitRequest updateFileNameLawSuitRequest) {
        lawSuitService.changeOriginalFileName(request, updateFileNameLawSuitRequest);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    private boolean isWordFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = getFileExtension(originalFilename);
            return extension != null && (
                    extension.equalsIgnoreCase("doc") ||
                            extension.equalsIgnoreCase("docx")
            );
        }
        return false;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOfDot + 1);
    }
}
