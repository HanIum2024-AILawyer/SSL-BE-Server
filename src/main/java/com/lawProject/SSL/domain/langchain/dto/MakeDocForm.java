package com.lawProject.SSL.domain.langchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeDocForm {
    private String doc_type; // '민사' 또는 '형사'
    private String defendant_count; // 피고 숫자 또는 피고소인 숫자
    private String case_description; // 사건에 대한 서술
    private String claim_amount; // 피해 금액 or 청구할 금액 (민사)
    private String damage_scale; // 피해 규모 (형사)
}
