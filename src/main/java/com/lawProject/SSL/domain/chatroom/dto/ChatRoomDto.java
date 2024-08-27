package com.lawProject.SSL.domain.chatroom.dto;

import jakarta.validation.constraints.NotNull;

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
