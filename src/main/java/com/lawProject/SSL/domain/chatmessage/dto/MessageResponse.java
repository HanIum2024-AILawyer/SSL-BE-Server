package com.lawProject.SSL.domain.chatmessage.dto;

import com.lawProject.SSL.domain.chatmessage.model.ChatMessage;
import com.lawProject.SSL.global.common.response.PageInfo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class MessageResponse {
    /**
     * Response
     */
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
            PageInfo pageInfo
    ) {
        public static ChatRoomMessageWithPageInfoResponse of(List<ChatRoomMessageResponse> chatRoomMessageResponses, PageInfo pageInfo) {
            return ChatRoomMessageWithPageInfoResponse.builder()
                    .chatRoomMessageResponses(chatRoomMessageResponses)
                    .pageInfo(pageInfo)
                    .build();
        }
    }
}
