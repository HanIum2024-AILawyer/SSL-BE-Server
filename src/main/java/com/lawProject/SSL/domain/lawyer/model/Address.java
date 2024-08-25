package com.lawProject.SSL.domain.lawyer.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

//변호사 상세-주소
@Embeddable
@Getter
public class Address {
    private String officeName;//사무실 이름
    private String officeAddress;//사무실 주소
    protected Address() {
    }

    @Builder
    public Address(String officeName, String officeAddress) {
        this.officeName = officeName;
        this.officeAddress = officeAddress;
    }
}

//기본코드
//변동사항 X