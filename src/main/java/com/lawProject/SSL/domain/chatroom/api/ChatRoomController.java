package com.lawProject.SSL.domain.chatroom.api;

import com.lawProject.SSL.domain.chatmessage.model.ChatMessage;
import com.lawProject.SSL.domain.chatroom.application.ChatRoomService;
import com.lawProject.SSL.domain.langchain.service.ChatService;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import com.lawProject.SSL.global.common.response.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.lawProject.SSL.domain.chatmessage.dto.MessageResponse.ChatRoomMessageResponse;
import static com.lawProject.SSL.domain.chatmessage.dto.MessageResponse.ChatRoomMessageWithPageInfoResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatroom")
public class ChatRoomController {
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createRoom(HttpServletRequest request) {
        String roomId = chatRoomService.createRoom(request);

        URI location = UriComponentsBuilder.newInstance()
                .path("/api/v1/chatroom/{roomId}")
                .buildAndExpand(roomId)
                .toUri();

        // 채팅방을 생성한 후 채팅방으로 이동
        return ApiResponse.onSuccess(SuccessCode._CREATED, location);
    }

    @GetMapping("/{room-id}")
    public ResponseEntity<ApiResponse<Object>> getChatRoomWithMessages(@PathVariable("room-id") String roomId,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          HttpServletRequest request
            ) {

        Page<ChatMessage> messages =
                chatService.getChatRoomMessages(roomId, page, size);
        PageInfo pageInfo = new PageInfo(page, size, (int)messages.getTotalElements(), messages.getTotalPages());

        List<ChatRoomMessageResponse> roomMessageResponses = messages.getContent().stream().map(
                ChatRoomMessageResponse::of
        ).toList();

        ChatRoomMessageWithPageInfoResponse chatRoomMessageWithPageInfoResponse = ChatRoomMessageWithPageInfoResponse.of(roomMessageResponses, pageInfo);

        return ApiResponse.onSuccess(SuccessCode._OK, chatRoomMessageWithPageInfoResponse);
    }


}
