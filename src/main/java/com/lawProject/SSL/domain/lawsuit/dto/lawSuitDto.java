package com.lawProject.SSL.domain.lawsuit.dto;

import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import lombok.Builder;

import java.time.LocalDateTime;

public class lawSuitDto {
    /**
     * Response
     */
    @Builder
    public record LawSuitResponse(
            Long id,

            String storedFileName,

            String originalFileName,
            LocalDateTime createAt
    ) {
        public static LawSuitResponse of(LawSuit lawSuit) {
            return LawSuitResponse.builder()
                    .id(lawSuit.getId())
                    .storedFileName(lawSuit.getStoredFileName())
                    .originalFileName(lawSuit.getOriginalFileName())
                    .createAt(lawSuit.getCreatedAt())
                    .build();
        }
    }
}
