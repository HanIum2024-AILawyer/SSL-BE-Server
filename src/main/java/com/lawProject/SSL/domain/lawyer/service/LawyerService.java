package com.lawProject.SSL.domain.lawyer.service;

import com.lawProject.SSL.domain.image.model.Image;
import com.lawProject.SSL.domain.image.service.ImageService;
import com.lawProject.SSL.domain.lawyer.exception.LawyerException;
import com.lawProject.SSL.domain.lawyer.model.Address;
import com.lawProject.SSL.domain.lawyer.model.ContactInfo;
import com.lawProject.SSL.domain.lawyer.model.HashTag;
import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import com.lawProject.SSL.domain.lawyer.repository.LawyerRepository;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.lawProject.SSL.domain.lawyer.dto.LawyerDto.LawyerCreateRequest;
import static com.lawProject.SSL.domain.lawyer.dto.LawyerDto.LawyerListResponse;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LawyerService {
    private final LawyerRepository lawyerRepository;
    private final ImageService imageService;

    /**
     * 변호사 생성 메서드
     * Admin만 가능한 기능
     */
    @Transactional
    public void create(LawyerCreateRequest request, MultipartFile image) throws IOException {
        Lawyer lawyer = createLawyer(request);

        lawyerRepository.save(lawyer);

        if (image != null && !image.isEmpty()) {
            String fileName = imageService.storeFile(image);
            Image lawyerProfileImage = Image.ofLawyer(lawyer, fileName);
            lawyer.setImage(lawyerProfileImage);
        }
    }

    /* 변호사 정보 업데이트 메서드 */
    @Transactional
    public void updateLawyer(Long lawyerId, LawyerCreateRequest request, MultipartFile image) { //Param: 파리미터로 넘어온 준영속 상태의 엔티티
        Lawyer findLawyer = findById(lawyerId);

        Address address = Address.builder()
                .officeName(request.officeName())
                .officeAddress(request.officeAddress())
                .build();

        ContactInfo contactInfo = ContactInfo.builder()
                .phoneNumber(request.phoneNumber())
                .faxNumber(request.faxNumber())
                .emailAddress(request.emailAddress())
                .build();

        HashTag hashtag = HashTag.builder().tagName(request.tagName()).build();

        findLawyer.update(address, contactInfo, hashtag, request);

        if (image != null && !image.isEmpty()) {
            String fileName = imageService.storeFile(image);
            Image lawyerImage = findLawyer.getImage();

            if (lawyerImage == null) {
                Image lawyerProfileImage = Image.ofLawyer(findLawyer, fileName);
                findLawyer.setImage(lawyerProfileImage);
            } else {
                lawyerImage.updateImageName(fileName);
            }
        }
    }

    /* 변호사 목록 조회 메서드 */
    public List<LawyerListResponse> findLawyers() {
        List<Lawyer> lawyers = lawyerRepository.findAll();

        return lawyers.stream()
                .map(LawyerListResponse::of)
                .toList();
    }

    /* 변호사 삭제 메서드 */
    @Transactional
    public void delete(Long lawyerId) {
        String imageName = findById(lawyerId).getImage().getImageName();
        lawyerRepository.deleteById(lawyerId);
        imageService.deleteFile(imageName);
    }


    /* 변호사 검색 메서드 */
    public List<LawyerListResponse> search(String keyword) {
        List<Lawyer> lawyerList = lawyerRepository.findByNameContains(keyword);
        return lawyerList.stream().map(LawyerListResponse::of).toList();
    }


    /**
     * Using Method
     */
    public Lawyer findById(Long lawyerId) {
        return lawyerRepository.findById(lawyerId)
                .orElseThrow(() -> new LawyerException(ErrorCode.LAWYER_NOT_FOUND));
    }

    private static Lawyer createLawyer(LawyerCreateRequest request) {
        Address address = Address.builder()
                .officeName(request.officeName())
                .officeAddress(request.officeAddress())
                .build();

        ContactInfo contactInfo = ContactInfo.builder()
                .phoneNumber(request.phoneNumber())
                .faxNumber(request.faxNumber())
                .emailAddress(request.emailAddress())
                .build();
        HashTag hashTag = HashTag.builder()
                .tagName(request.tagName())
                .build();

        return Lawyer.builder()
                .name(request.name())
                .intro(request.intro())
                .businessRegistrationNumber(request.businessRegistrationNumber())
                .address(address)
                .contactInfo(contactInfo)
                .hashTag(hashTag)
                .build();
    }
}