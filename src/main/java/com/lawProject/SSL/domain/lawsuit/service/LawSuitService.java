package com.lawProject.SSL.domain.lawsuit.service;

import com.lawProject.SSL.domain.lawsuit.dto.FileStorageResult;
import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import com.lawProject.SSL.domain.lawsuit.repository.LawSuitRepository;
import com.lawProject.SSL.domain.user.application.UserService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static com.lawProject.SSL.domain.lawsuit.dto.lawSuitDto.LawSuitResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class LawSuitService {
    private final FileService fileService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final LawSuitRepository lawSuitRepository;

    @Transactional
    public FileStorageResult uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {
        FileStorageResult fileStorageResult = fileService.storeFile(file);
        User user = jwtUtil.getUserFromRequest(request);

        if (file != null && !file.isEmpty()) {
            LawSuit lawSuit = LawSuit.ofUser(user, fileStorageResult);
            lawSuit.setExpireTime(LocalDateTime.now().plusDays(7));
            lawSuitRepository.save(lawSuit);
            return fileStorageResult;
        }

        throw new FileException(ErrorCode.FILE_NOT_UPLOADED);
    }

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

    public List<LawSuitResponse> getLawSuitList(HttpServletRequest request) {
        User user = userService.getUserInfo(request);
        Long userId = user.getId();
        List<LawSuit> lawSuitList = lawSuitRepository.findAllByUserIdOrderByIdDesc(userId);

        List<LawSuitResponse> lawSuitResponseList = lawSuitList.stream().map(
                LawSuitResponse::of
        ).toList();
        return lawSuitResponseList;
    }
}
