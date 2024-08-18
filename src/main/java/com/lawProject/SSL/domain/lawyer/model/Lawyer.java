package com.lawProject.SSL.domain.lawyer.model;

import com.lawProject.SSL.domain.lawyer.dto.LawyerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//변호사 정보

@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Lawyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lawyer_id")
    private Long id;
    private String name;
    private String businessRegistrationNumber; //사업자 등록 번호

    @Embedded
    private Address address; //물리적 주소
    @Embedded
    private ContactInfo contactInfo; //연락 수단
    @Embedded
    private  LawyerTag lawyerTag;

    /* Using Method */
    public void update(Address address, ContactInfo contactInfo, LawyerDto.LawyerForm form) {
        this.address = address;
        this.contactInfo = contactInfo;
        this.name = form.name();
        this.businessRegistrationNumber = form.businessRegistrationNumber();
    }
}
//기본코드
//서비스 추가 필요

/*
비교용 코드
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lawyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lawyer_id")
    private Long id;

    @NotNull
    private String name;

//    @NotNull
//    private Field field;

    private String details;

}
*/

