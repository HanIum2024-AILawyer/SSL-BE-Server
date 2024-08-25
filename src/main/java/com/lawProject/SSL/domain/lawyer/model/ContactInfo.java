package com.lawProject.SSL.domain.lawyer.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

//변호사 상세-연락처
@Embeddable
@Getter
public class ContactInfo {
    private String phoneNumber;//핸드폰 번호
    private String faxNumber;//팩스 번호
    private String emailAddress;//이메일 주소
    protected ContactInfo() {
    }

    @Builder
    public ContactInfo(String phoneNumber, String faxNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.emailAddress = emailAddress;
    }
}

//기본코드
//변동사항 X