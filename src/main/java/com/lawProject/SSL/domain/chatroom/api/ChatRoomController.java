package com.lawProject.SSL.domain.chatroom.api;

import com.lawProject.SSL.domain.langchain.domain.ChatMessage;
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

import static com.lawProject.SSL.domain.langchain.dto.ChatMessageDto.ChatRoomMessageResponse;
import static com.lawProject.SSL.domain.langchain.dto.ChatMessageDto.ChatRoomMessageWithPageInfoResponse;

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

        // 이때 프론트엔드는 roomId를 뽑아서 해당 채팅방을 "stompClient.subscribe('/sub/chats/' + roomId, function(message)" 처럼 구독해야 됨

        // 채팅방을 생성한 후 채팅방으로 이동
        return ApiResponse.onSuccess(SuccessCode._CREATED, location);
    }

    // 채팅방 열기
    @GetMapping("/{room-id}")
    public ResponseEntity<ApiResponse<Object>> getChatRoomWithMessages(@PathVariable("room-id") String roomId,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          HttpServletRequest request
            ) {

        // page, size 유효성 검정
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        Page<ChatMessage> messages =
                chatService.getChatRoomMessages(roomId, page, size);
        PageInfo pageInfo = new PageInfo(page, size, (int)messages.getTotalElements(), messages.getTotalPages());

        List<ChatRoomMessageResponse> roomMessageResponses = messages.getContent().stream().map(
                ChatRoomMessageResponse::of
        ).toList();

        Long userId = userService.getUserInfo(request).getId();

        ChatRoomMessageWithPageInfoResponse chatRoomMessageWithPageInfoResponse = ChatRoomMessageWithPageInfoResponse.of(roomMessageResponses, pageInfo, userId);

        return ApiResponse.onSuccess(SuccessCode._OK, chatRoomMessageWithPageInfoResponse);
    }


}
