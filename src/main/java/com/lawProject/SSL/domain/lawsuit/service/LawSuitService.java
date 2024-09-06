package com.lawProject.SSL.domain.lawsuit.service;

import com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto;
import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import com.lawProject.SSL.domain.lawsuit.repository.LawSuitRepository;
import com.lawProject.SSL.domain.user.exception.UserException;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.LawSuitResponse;
import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.UpdateFileNameLawSuitRequest;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LawSuitService {
    private final FileService fileService;
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

    /* 소송장 목록 조회 메서드 */
    public List<LawSuitResponse> getLawSuitList(User user) {
        List<LawSuit> lawSuitList = user.getLawSuitList();

        return lawSuitList.stream().map(
                LawSuitResponse::of
        ).toList();
    }

    /* 소송장 이름 변경 메서드 */
    @Transactional
    public void changeOriginalFileName(User user, UpdateFileNameLawSuitRequest changeLawSuitRequest) {
        LawSuit lawSuit = findLawSuitById(changeLawSuitRequest.lawSuitId());
        String originalFileName = lawSuit.getOriginalFileName();

        if (!lawSuit.getUser().equals(user)) {
            throw new UserException(ErrorCode.USER_MISMATCH);
        }
        String newOriginalFileName = generateNewOriginalFileName(originalFileName, changeLawSuitRequest.updateOriginalFileName());
        lawSuit.setOriginalFileName(newOriginalFileName);
    }

    /* 소송장 삭제 메서드 */
    @Transactional
    public void deleteSuit(lawSuitDto.DeleteSuitRequest deleteSuitRequest) {
        List<Long> lawSuitIdList = deleteSuitRequest.lawSuitIdList();
        if (lawSuitIdList != null && !lawSuitIdList.isEmpty()) {
            lawSuitRepository.deleteAllById(lawSuitIdList);
        }

        List<String> storedFileNames = lawSuitIdList.stream()
                .map(id ->
                {
                    return findLawSuitById(id).getStoredFileName();
                })
                .toList();

        fileService.deleteFiles(storedFileNames);
        log.info("파일 저장소에서 파일 삭제 완료");
    }

    /* 만료 기한이 지난 소송장 자동 삭제 메서드*/
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정에 실행
    public void deleteExpiredLawSuits() {
        LocalDateTime now = LocalDateTime.now();
        List<LawSuit> expiredLawSuits = lawSuitRepository.findByExpireTimeBefore(now);
        lawSuitRepository.deleteAll(expiredLawSuits);
        List<String> storeFileNames = expiredLawSuits.stream()
                .map(s -> s.getStoredFileName())
                .toList();
        fileService.deleteFiles(storeFileNames);
        log.info("만료 기한이 지난 파일 삭제");
    }

    /* Using Method */
    private String generateNewOriginalFileName(String originalFileName, String newFileNameWithoutExt) {
        String extension = extractFileExtension(originalFileName);
        return newFileNameWithoutExt + "." + extension;
    }

    public String extractFileExtension(String filename) {
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos + 1);
    }

    public LawSuit findLawSuitById(Long id) {
        return lawSuitRepository.findById(id).orElseThrow(
                () -> new FileException(ErrorCode.FILE_NOT_FOUND)
        );
    }

    public String getOriginalFileName(String storedFileName) {
        LawSuit lawSuit = lawSuitRepository.findByStoredFileName(storedFileName)
                .orElseThrow(() -> new FileException(ErrorCode.FILE_NOT_FOUND));
        return lawSuit.getOriginalFileName();
    }
}
