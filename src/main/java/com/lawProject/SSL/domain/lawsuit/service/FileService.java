package com.lawProject.SSL.domain.lawsuit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<String> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                imageUrlList.add(storeFile(multipartFile)); //파일의 이름 정보가 들어간 UploadFile 객체를 storeFileResult에 넣어줌
            }
        }
        return imageUrlList;

    }

    public String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Empty file.");
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = createServerFileName(originalFilename); //랜덤의 uuid를 추가한 파일 이름
        String fullPath = getFullPath(fileName);
        multipartFile.transferTo(new File(fullPath));

        return fullPath;
    }

    // 서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID를 사용해서 충돌하지 않도록 한다.
    private String createServerFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString(); //파일 이름 중복 방지
        return uuid + "." + ext;
    }

    //확장자를 별도로 추출해서 서버 내부에서 관리하는 파일명에도 붙여준다.
    //Ex) a.png라는 이름으로 업로드하면 2def12-42qd-3214-e2dqda2.png 와 같이 확장자를 추가해서 저장한다.
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf("."); //파일의 확장자 추출 ex) .png .img
        return originalFilename.substring(pos + 1);
    }
}