package com.lawProject.SSL.domain.langchain.dto;

import com.lawProject.SSL.domain.lawsuit.dto.FileStorageResult;
import lombok.Builder;
import org.springframework.core.io.Resource;

public class AiDto {

    /**
     * Response
     */
    public record AiDocResponse(
        Resource wordFile,
        String docType,
        String aiComment
    ) {
    }

    @Builder
    public record FileStorageResultWithComment(
            String originalFileName,
            String storedFileName,
            String aiComment
    ) {
        public static FileStorageResultWithComment of(FileStorageResult fileStorageResult, String aiComment) {
            return FileStorageResultWithComment.builder()
                    .originalFileName(fileStorageResult.getOriginalFileName())
                    .storedFileName(fileStorageResult.getStoredFileName())
                    .aiComment(aiComment)
                    .build();
        }
    }
}
