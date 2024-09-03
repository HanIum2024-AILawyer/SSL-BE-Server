package com.lawProject.SSL.domain.chatroom.dto;

import com.lawProject.SSL.domain.langchain.dto.ChatMessageDto;
import com.lawProject.SSL.global.common.response.PageInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

public class ChatRoomDto {

    /**
     * Request
     */
    public record ChatRoomDeleteRequest(
            @NotNull
            String roomId
    ) {
    }

    /**
     * Response
     */
    public record ChatRoomDeleteResponse(
            boolean success
    ) {

    }

    @Builder
    public record ChatRoomMessageWithPageInfoResponse(
            List<ChatMessageDto.ChatRoomMessageResponse> chatRoomMessageResponses,
            PageInfo pageInfo,
            Long userId
    ) {
        public static ChatRoomMessageWithPageInfoResponse of(List<ChatMessageDto.ChatRoomMessageResponse> chatRoomMessageResponses, PageInfo pageInfo, Long userId) {
            return ChatRoomMessageWithPageInfoResponse.builder()
                    .chatRoomMessageResponses(chatRoomMessageResponses)
                    .pageInfo(pageInfo)
                    .userId(userId)
                    .build();
        }
    }
}
