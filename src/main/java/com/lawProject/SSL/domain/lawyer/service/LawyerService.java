package com.lawProject.SSL.domain.lawyer.service;

import com.lawProject.SSL.domain.image.model.Image;
import com.lawProject.SSL.domain.lawsuit.dto.FileStorageResult;
import com.lawProject.SSL.domain.lawsuit.service.FileService;
import com.lawProject.SSL.domain.lawyer.exception.LawyerException;
import com.lawProject.SSL.domain.lawyer.model.Address;
import com.lawProject.SSL.domain.lawyer.model.ContactInfo;
import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import com.lawProject.SSL.domain.lawyer.repository.LawyerRepository;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.lawProject.SSL.domain.lawyer.dto.LawyerDto.LawyerCreateRequest;
import static com.lawProject.SSL.domain.lawyer.dto.LawyerDto.LawyerListResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class LawyerService {
    private final LawyerRepository lawyerRepository;
    private final FileService fileService;

    /**
     * 변호사 생성 메서드
     * Admin만 가능한 기능
     */
    @Transactional
    public void create(LawyerCreateRequest request, MultipartFile image) {
        Lawyer lawyer = createLawyer(request);

        lawyerRepository.save(lawyer);

        Image lawyerImage = createLawyerProfileImage(image, lawyer);
        lawyer.setImage(lawyerImage);
    }

    /**
     * 변호사 정보 업데이트 메서드
     */
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

        if (image.isEmpty()) {
            /* Dirty Checking 사용 */
            findLawyer.update(address, contactInfo, request);
        } else {
            Image lawyerProfileImage = createLawyerProfileImage(image, findLawyer);
            findLawyer.updateWithImage(address, contactInfo, request, lawyerProfileImage);
        }
    }

    /**
     * 변호사 찾기 메서드
     */
    public List<LawyerListResponse> findLawyers() {
        List<Lawyer> lawyers = lawyerRepository.findAll();

        return lawyers.stream()
                .map(LawyerListResponse::of)
                .toList();
    }

    /**
     * Using Method
     */
    public Lawyer findById(Long lawyerId) {
        return lawyerRepository.findById(lawyerId)
                .orElseThrow(() -> new LawyerException(ErrorCode.LAWYER_NOT_FOUND));
    }

    private Image createLawyerProfileImage(MultipartFile image, Lawyer lawyer) {
        FileStorageResult fileStorageResult = fileService.storeFile(image);
        Image lawyerImage = Image.ofLawyer(lawyer, fileStorageResult.getStoredFileName());
        return lawyerImage;
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

        Lawyer lawyer = Lawyer.builder()
                .name(request.name())
                .lawyerTag(request.tag())
                .address(address)
                .contactInfo(contactInfo)
                .build();
        return lawyer;
    }
}

//기본코드
//서비스 추가 필요