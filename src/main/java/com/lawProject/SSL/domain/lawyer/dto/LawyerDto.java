package com.lawProject.SSL.domain.lawyer.dto;

import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import lombok.Builder;


//Lawyer에 대한 Dto

public class LawyerDto {

    /* 여기서 사용하는 record는 Java17부터 생긴 기능.
     DTO를 저격해서 나온 기능이다.
     불변의 속성을 가졌고, 기본적으로 Getter가 적용되어 있고, 한 눈에 보기 편하다는 장점이 있음 */

    /**
     * Request
     */
    public record LawyerCreateRequest(
            String name,
            String intro,
            String tagName,
            String businessRegistrationNumber,
            String officeName,
            String officeAddress,
            String phoneNumber,
            String faxNumber,
            String emailAddress
    ) {

    }

    /**
     * Response
     */
    @Builder
    public record LawyerListResponse(
            Long id,
            String name,
            String tagName,
            String imageName
    ) {
        public static LawyerListResponse of(Lawyer lawyer) {
            return LawyerListResponse.builder()
                    .id(lawyer.getId())
                    .name(lawyer.getName())
                    .tagName(lawyer.getHashTag().getTagName())
                    .imageName(lawyer.getImage().getImageName())
                    .build();
        }
    }

    @Builder
    public record LawyerDetailResponse(
            Long id,
            String name,
            String businessRegistrationNumber,
            String officeName, //사무실 이름
            String officeAddress,//사무실 주소
            String phoneNumber, //핸드폰 번호
            String faxNumber, //팩스 번호
            String emailAddress, //이메일 주소
            String tagName, //태그
            String imageName // 이미지 이름
    ) {
        public static LawyerDetailResponse of(Lawyer lawyer) {
            return LawyerDetailResponse.builder()
                    .id(lawyer.getId())
                    .name(lawyer.getName())
                    .businessRegistrationNumber(lawyer.getBusinessRegistrationNumber())
                    .officeName(lawyer.getAddress().getOfficeName())
                    .officeAddress(lawyer.getAddress().getOfficeAddress())
                    .phoneNumber(lawyer.getContactInfo().getPhoneNumber())
                    .faxNumber(lawyer.getContactInfo().getFaxNumber())
                    .emailAddress(lawyer.getContactInfo().getEmailAddress())
                    .tagName(lawyer.getHashTag().getTagName())
                    .imageName(lawyer.getImage().getImageName())
                    .build();
        }
    }

}

//기본코드
//서비스 추가 필요