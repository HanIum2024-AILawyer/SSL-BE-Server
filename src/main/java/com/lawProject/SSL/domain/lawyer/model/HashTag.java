package com.lawProject.SSL.domain.lawyer.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

//해시태그 기능
@Embeddable
@Getter
public class HashTag {
    private String tagName;
    protected HashTag(){}
    @Builder
    public HashTag(String tagName){
        this.tagName = tagName;
    }
}
//개발 확인을 위해 String으로 먼저 진행 추후 list변경 가능