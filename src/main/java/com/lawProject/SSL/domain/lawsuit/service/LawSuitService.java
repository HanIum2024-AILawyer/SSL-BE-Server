package com.lawProject.SSL.domain.lawsuit.service;

import com.lawProject.SSL.domain.lawsuit.exception.FileException;
import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import com.lawProject.SSL.domain.lawsuit.repository.LawSuitRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LawSuitService {
    private final FileService fileService;
    private final JwtUtil jwtUtil;
    private final LawSuitRepository lawSuitRepository;

    @Transactional
    public String uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {
        String fileUrl = fileService.storeFile(file);
        User user = jwtUtil.getUserFromRequest(request);

        if (file != null && !file.isEmpty()) {
            LawSuit lawSuit = LawSuit.ofUser(user, fileUrl);
            lawSuit.setExpireTime(LocalDateTime.now().plusDays(7));
            lawSuitRepository.save(lawSuit);
            return fileUrl;
        }

        throw new FileException(ErrorCode.FILE_NOT_UPLOADED);
    }

    public UrlResource downloadFile(String filename) throws MalformedURLException {
        Path filePath = Paths.get(fileService.getFullPath(filename)).toAbsolutePath().normalize();
        UrlResource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new MalformedURLException("File not found " + filename);
        }


    }
}
