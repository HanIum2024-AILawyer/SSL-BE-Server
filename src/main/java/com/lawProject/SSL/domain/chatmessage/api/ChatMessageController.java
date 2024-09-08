package com.lawProject.SSL.domain.chatmessage.api;

import com.lawProject.SSL.domain.chatmessage.dto.ChatMessageDto;
import com.lawProject.SSL.domain.chatmessage.service.ChatMessageService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.annotation.CurrentUser;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    /* 채팅방 메시지 목록 조회 */
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Object>> openChatRoom(@PathVariable("roomId") String roomId,
                                                            @RequestParam(name="lastMessageId", required = false) Long lastMessageId,
                                                            @RequestParam(name="size" ,defaultValue = "30") int size,
                                                            @CurrentUser User user
    ) {
        List<ChatMessageDto.ChatRoomMessageResponse> messagesForInfiniteScroll = chatMessageService.getMessagesForInfiniteScroll(roomId, lastMessageId, size, user);

        return ApiResponse.onSuccess(SuccessCode._OK, messagesForInfiniteScroll);
    }
}
