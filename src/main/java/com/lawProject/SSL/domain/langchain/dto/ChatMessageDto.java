package com.lawProject.SSL.domain.langchain.dto;

import com.lawProject.SSL.domain.langchain.domain.ChatMessage;
import com.lawProject.SSL.domain.langchain.domain.SenderType;
import com.lawProject.SSL.global.common.response.PageInfo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageDto {
    /**
     * Request
     */
    @Builder
    public record ChatRequest(
            String roomId,
            String content,
            SenderType senderType
    ) {

    }

    /**
     * Response
     */
    @Builder
    public record ChatResponse(
            String roomId,
            String content,
            SenderType senderType
    ) {

    }

    @Builder
    public record ChatRoomMessageResponse( // 채팅방 속 하나의 메시지
                                           Long messageId,
                                           Long senderId,
                                           String content,
                                           String senderType,
                                           LocalDateTime sendTime
    ) {
        public static ChatRoomMessageResponse of(ChatMessage chatMessage) {
            return ChatRoomMessageResponse.builder()
                    .messageId(chatMessage.getId())
                    .senderId(chatMessage.getUser().getId())
                    .content(chatMessage.getContent())
                    .senderType(chatMessage.getSenderType().toString())
                    .sendTime(chatMessage.getCreatedAt())
                    .build();
        }
    }

    @Builder
    public record ChatRoomMessageWithPageInfoResponse(
            List<ChatRoomMessageResponse> chatRoomMessageResponses,
            PageInfo pageInfo,
            Long userId
    ) {
        public static ChatRoomMessageWithPageInfoResponse of(List<ChatRoomMessageResponse> chatRoomMessageResponses, PageInfo pageInfo, Long userId) {
            return ChatRoomMessageWithPageInfoResponse.builder()
                    .chatRoomMessageResponses(chatRoomMessageResponses)
                    .pageInfo(pageInfo)
                    .userId(userId)
                    .build();
        }
    }
}
