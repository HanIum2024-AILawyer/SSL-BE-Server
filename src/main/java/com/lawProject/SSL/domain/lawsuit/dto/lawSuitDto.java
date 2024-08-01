package com.lawProject.SSL.domain.lawsuit.dto;

import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class lawSuitDto {
    /**
     * Request
     */
    public record UpdateFileNameLawSuitRequest(
            Long lawSuitId,
            String updateOriginalFileName
    ) {
    }

    public record DeleteSuitRequest(
            List<Long> lawSuitIdList
    ) {
    }




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
