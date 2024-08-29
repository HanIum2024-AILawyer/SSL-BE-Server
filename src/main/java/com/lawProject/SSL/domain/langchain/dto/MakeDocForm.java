package com.lawProject.SSL.domain.langchain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeDocForm {
    @NotNull
    private String fileName;
    @NotNull
    private String name;
    @NotNull
    private String content;
    //TODO AI 파트와 상의 후 수정 필요
}
