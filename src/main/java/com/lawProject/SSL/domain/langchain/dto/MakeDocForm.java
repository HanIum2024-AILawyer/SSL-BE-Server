package com.lawProject.SSL.domain.langchain.dto;

import com.lawProject.SSL.domain.lawsuit.model.LawsuitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeDocForm {
    private LawsuitType lawsuitType; // '민사' 또는 '형사'
    private int defendantCount; // 피고 숫자 또는 피고소인 숫자
    private String caseDescription; // 사건에 대한 서술
    private BigDecimal claimAmount; // 피해 금액 or 청구할 금액 (민사)
    private String damageScale; // 피해 규모 (형사)

    /*
    BigDecimal 타입 사용 이유
    - float, double 타입은 부동소수점 연산을 사용하는데 정밀도가 떨어질 수 있다.
    하지만 BigDecimal 타입은 소수점 이하 자릿수의 정확성을 유지하면서 연산을 할 수 있어 정밀한 결과를 보장한다.
     */
}
