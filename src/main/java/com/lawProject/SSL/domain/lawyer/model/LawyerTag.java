package com.lawProject.SSL.domain.lawyer.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;


//변호사 태그 추가시 활용
@Embeddable
@Getter
public class LawyerTag {
    String tagName;
    protected LawyerTag(){
    }
    public LawyerTag(String tagName){
        this.tagName = tagName;
    }
}
//기본코드
//필요시 추가