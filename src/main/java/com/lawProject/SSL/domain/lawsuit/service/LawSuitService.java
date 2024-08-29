package com.lawProject.SSL.domain.lawsuit.service;

import com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto;
import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import com.lawProject.SSL.domain.lawsuit.repository.LawSuitRepository;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.domain.user.exception.UserException;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.LawSuitResponse;
import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.UpdateFileNameLawSuitRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LawSuitService {
    private final FileService fileService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final LawSuitRepository lawSuitRepository;

    /* 소송장 다운로드 매서드 */
    public Resource downloadFile(String filename) throws MalformedURLException {
        Path filePath = Paths.get(fileService.getFullPath(filename)).toAbsolutePath().normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new MalformedURLException("File not found " + filename);
        }
    }

    public String getOriginalFileName(String storedFileName) {
        LawSuit lawSuit = lawSuitRepository.findByStoredFileName(storedFileName)
                .orElseThrow(() -> new FileException(ErrorCode.FILE_NOT_FOUND));
        return lawSuit.getOriginalFileName();
    }

    /* 소송장 목록 조회 메서드 */
    public List<LawSuitResponse> getLawSuitList(HttpServletRequest request) {
        User user = userService.getUserInfo(request);
//        List<LawSuit> lawSuitList = lawSuitRepository.findAllByUserIdOrderByIdDesc(userId);
        List<LawSuit> lawSuitList = user.getLawSuitList();

        return lawSuitList.stream().map(
                LawSuitResponse::of
        ).toList();
    }

    /* 소송장 이름 변경 메서드 */
    @Transactional
    public void changeOriginalFileName(HttpServletRequest request, UpdateFileNameLawSuitRequest changeLawSuitRequest) {
        LawSuit lawSuit = findLawSuitById(changeLawSuitRequest.lawSuitId());
        String originalFileName = lawSuit.getOriginalFileName();
        User user = userService.getUserInfo(request);
        if (!lawSuit.getUser().getId().equals(user.getId())) {
            throw new UserException(ErrorCode.USER_MISMATCH);
        }
        String newOriginalFileName = generateNewOriginalFileName(originalFileName, changeLawSuitRequest.updateOriginalFileName());
        lawSuit.setOriginalFileName(newOriginalFileName);
    }

    private String generateNewOriginalFileName(String originalFileName, String newFileNameWithoutExt) {
        String extension = extractFileExtension(originalFileName);
        return newFileNameWithoutExt + "." + extension;
    }

    private String extractFileExtension(String filename) {
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos + 1);
    }

    public LawSuit findLawSuitById(Long id) {
        return lawSuitRepository.findById(id).orElseThrow(
                () -> new FileException(ErrorCode.FILE_NOT_FOUND)
        );
    }

    /* 소송장 삭제 메서드 */
    @Transactional
    public void deleteSuit(lawSuitDto.DeleteSuitRequest deleteSuitRequest) {
        List<Long> lawSuitIdList = deleteSuitRequest.lawSuitIdList();
        if (lawSuitIdList != null && !lawSuitIdList.isEmpty()) {
            lawSuitRepository.deleteAllById(lawSuitIdList);
        }
    }
}
