package com.lawProject.SSL.domain.inquery.dto;

import com.lawProject.SSL.domain.inquery.model.InQuery;
import com.lawProject.SSL.domain.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class InQueryDto {

    /**
     * Request
     */
    public record InQueryWriteRequest(
            @NotNull
            String title,
            @NotNull
            String content
    ) {
        public InQuery toEntity(User user) {
            return InQuery.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }
    }

    /**
     * Response
     */
    @Builder
    public record InQueryDetailResponse(
            String title,
            String content,
            String answer,
            String userName
    ) {
        public static InQueryDetailResponse of(InQuery inQuery) {
            return InQueryDetailResponse.builder()
                    .title(inQuery.getTitle())
                    .content(inQuery.getContent())
                    .answer(inQuery.getAnswer())
                    .userName(inQuery.getUser().getName())
                    .build();
        }
    }

    @Builder
    public record InQueryListResponse(
            String title,
            String content,
            boolean isAnswer
    ) {
        public static InQueryListResponse of(InQuery inQuery) {
            return InQueryListResponse.builder()
                    .title(inQuery.getTitle())
                    .content(inQuery.getContent())
                    .isAnswer(inQuery.getIsAnswer())
                    .build();
        }
    }
}

