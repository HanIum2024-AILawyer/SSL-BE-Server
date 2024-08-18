package com.lawProject.SSL.domain.lawyer.service;

import com.lawProject.SSL.domain.lawyer.model.Address;
import com.lawProject.SSL.domain.lawyer.model.ContactInfo;
import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import com.lawProject.SSL.domain.lawyer.dto.LawyerDto;
import com.lawProject.SSL.domain.lawyer.repository.LawyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//lawyer 서비스 담당

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class LawyerService {
    private final LawyerRepository lawyerRepository;

    /**
     * 변호사 생성 메서드
     * 본래 Admin만 가능한 기능
     */
    @Transactional
    public void saveLawyer(LawyerDto.LawyerForm form) {

        Address address = new Address(form.officeName(), form.officeAddress());
        ContactInfo contactInfo = new ContactInfo(form.phoneNumber(), form.faxNumber(), form.emailAddress());

        Lawyer lawyer = Lawyer.builder()
                .name(form.name())
                .address(address)
                .contactInfo(contactInfo)
                .build();

        lawyerRepository.save(lawyer);
    }

    /**
     * 변호사 정보 업데이트 메서드
     */
    @Transactional
    public void updateLawyer(Long lawyerId, LawyerDto.LawyerForm form) { //Param: 파리미터로 넘어온 준영속 상태의 엔티티
        Lawyer findLawyer = findById(lawyerId);

        Address address = new Address(form.officeName(), form.officeAddress());
        ContactInfo contactInfo = new ContactInfo(form.phoneNumber(), form.faxNumber(), form.emailAddress());

        /* Dirty Checking 사용 */
        findLawyer.update(address, contactInfo, form);
    }

    /**
     * 변호사 찾기 메서드
     */
    public List<LawyerDto.LawyerListResponse> findLawyers() {
        List<Lawyer> lawyers = lawyerRepository.findAll();

        List<LawyerDto.LawyerListResponse> lawyerListResponses = lawyers.stream().map(l -> LawyerDto.LawyerListResponse.of(l)).toList();

        return lawyerListResponses;
    }

    /**
     * Using Method
     */
    public Lawyer findById(Long lawyerId) {
        return lawyerRepository.findById(lawyerId)
                .orElseThrow(() -> new NullPointerException());
    }
}

//기본코드
//서비스 추가 필요