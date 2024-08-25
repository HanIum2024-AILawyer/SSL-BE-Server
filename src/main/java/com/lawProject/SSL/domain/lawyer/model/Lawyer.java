package com.lawProject.SSL.domain.lawyer.model;

import com.lawProject.SSL.domain.image.model.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.lawProject.SSL.domain.lawyer.dto.LawyerDto.LawyerCreateRequest;

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
    private String intro; // 변호사 본인 소개
    private String lawyerTag; // 변호사 분야

    @Embedded
    private Address address; //물리적 주소
    @Embedded
    private ContactInfo contactInfo; //연락 수단

    @OneToOne(mappedBy = "lawyer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Image image;

    /* Using Method */
    public void update(Address address, ContactInfo contactInfo, LawyerCreateRequest request) {
        this.address = address;
        this.contactInfo = contactInfo;
        this.lawyerTag = request.tag();
        this.name = request.name();
        this.businessRegistrationNumber = request.businessRegistrationNumber();
    }

    public void updateWithImage(Address address, ContactInfo contactInfo, LawyerCreateRequest request, Image image) {
        this.address = address;
        this.contactInfo = contactInfo;
        this.lawyerTag = request.tag();
        this.name = request.name();
        this.businessRegistrationNumber = request.businessRegistrationNumber();
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}

