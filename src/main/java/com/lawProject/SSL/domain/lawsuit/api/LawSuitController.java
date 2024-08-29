package com.lawProject.SSL.domain.lawsuit.api;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lawsuit")
public class LawSuitController {
    private final LawSuitService lawSuitService;
    private final FileService fileService;

    /* 소송장 다운로드 */
    // TODO originalFileName으로 변경시 정상적으로 디코딩 안되는 문제 해결, 파일 이름 앞에 userId 등 식별자 추가
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

    /* 소송장 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getLawSuitList(HttpServletRequest request) {
        List<LawSuitResponse> lawSuitList = lawSuitService.getLawSuitList(request);
        return ApiResponse.onSuccess(SuccessCode._OK, lawSuitList);
    }

    /* 소송장 이름 변경 */
    @PatchMapping
    public ResponseEntity<ApiResponse<Object>> changeOriginalFileName(HttpServletRequest request, @RequestBody UpdateFileNameLawSuitRequest updateFileNameLawSuitRequest) {
        lawSuitService.changeOriginalFileName(request, updateFileNameLawSuitRequest);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /* 소송장 삭제 */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> deleteSuit(HttpServletRequest request, @RequestBody DeleteSuitRequest deleteSuitRequest) {
        lawSuitService.deleteSuit(deleteSuitRequest);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOfDot + 1);
    }
}
