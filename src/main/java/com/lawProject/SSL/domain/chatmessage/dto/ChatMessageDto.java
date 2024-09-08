package com.lawProject.SSL.domain.chatmessage.dto;

import com.lawProject.SSL.domain.chatmessage.model.ChatMessage;
import com.lawProject.SSL.domain.chatmessage.model.SenderType;
import lombok.Builder;

import java.time.LocalDateTime;

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
                                           String content,
                                           String senderType,
                                           LocalDateTime sendTime
    ) {
        public static ChatRoomMessageResponse of(ChatMessage chatMessage) {
            return ChatRoomMessageResponse.builder()
                    .messageId(chatMessage.getId())
                    .content(chatMessage.getContent())
                    .senderType(chatMessage.getSenderType().toString())
                    .sendTime(chatMessage.getCreatedAt())
                    .build();
        }
    }
}
