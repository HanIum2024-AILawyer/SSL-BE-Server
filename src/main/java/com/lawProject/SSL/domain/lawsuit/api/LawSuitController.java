package com.lawProject.SSL.domain.lawsuit.api;

import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.service.FileService;
import com.lawProject.SSL.domain.lawsuit.service.LawSuitService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.annotation.CurrentUser;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
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

            String extension = lawSuitService.extractFileExtension(originalFileName);

            MediaType mediaType = switch (extension.toLowerCase()) {
                case "doc" -> MediaType.parseMediaType("application/msword");
                case "docx" -> MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                default -> MediaType.APPLICATION_OCTET_STREAM;
            };

            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s", encodedFileName, encodedFileName));

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
    public ResponseEntity<ApiResponse<Object>> getLawSuitList(@CurrentUser User user) {
        List<LawSuitResponse> lawSuitList = lawSuitService.getLawSuitList(user);
        return ApiResponse.onSuccess(SuccessCode._OK, lawSuitList);
    }

    /* 소송장 이름 변경 */
    @PatchMapping
    public ResponseEntity<ApiResponse<Object>> changeOriginalFileName(@CurrentUser User user, @RequestBody UpdateFileNameLawSuitRequest updateFileNameLawSuitRequest) {
        lawSuitService.changeOriginalFileName(user, updateFileNameLawSuitRequest);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /* 소송장 삭제 */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> deleteSuit(@RequestBody DeleteSuitRequest deleteSuitRequest) {
        lawSuitService.deleteSuit(deleteSuitRequest);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }
}
