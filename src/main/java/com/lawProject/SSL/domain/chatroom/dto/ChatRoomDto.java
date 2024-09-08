package com.lawProject.SSL.domain.chatroom.dto;

import com.lawProject.SSL.domain.chatmessage.dto.ChatMessageDto;
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
}
