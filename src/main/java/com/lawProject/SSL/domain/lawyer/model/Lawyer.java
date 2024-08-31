package com.lawProject.SSL.domain.lawyer.model;

import com.lawProject.SSL.domain.image.model.Image;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import static com.lawProject.SSL.domain.lawyer.dto.LawyerDto.LawyerCreateRequest;

//변호사 정보

@Getter
@DiscriminatorColumn(name = "dtype")
@Entity
public class Lawyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lawyer_id")
    private Long id;
    private String name;
    private String businessRegistrationNumber; //사업자 등록 번호
    private String intro; // 변호사 본인 소개

    @Embedded
    private Address address; //물리적 주소
    @Embedded
    private ContactInfo contactInfo; //연락 수단
    @Embedded
    private HashTag hashTag;

    @OneToOne(mappedBy = "lawyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    public Lawyer() {

    }

    /* Using Method */
    public void update(Address address, ContactInfo contactInfo, HashTag hashTag, LawyerCreateRequest request) {
        this.address = address;
        this.contactInfo = contactInfo;
        this.hashTag = hashTag;
        this.name = request.name();
        this.businessRegistrationNumber = request.businessRegistrationNumber();
    }

    public void updateWithImage(Address address, ContactInfo contactInfo, HashTag hashTag, LawyerCreateRequest request, Image image) {
        this.address = address;
        this.contactInfo = contactInfo;
        this.hashTag = hashTag;
        this.name = request.name();
        this.businessRegistrationNumber = request.businessRegistrationNumber();
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Builder
    public Lawyer(String name, String businessRegistrationNumber, String intro, HashTag hashTag, Address address, ContactInfo contactInfo) {
        this.name = name;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.intro = intro;
        this.hashTag = hashTag;
        this.address = address;
        this.contactInfo = contactInfo;
    }
}

