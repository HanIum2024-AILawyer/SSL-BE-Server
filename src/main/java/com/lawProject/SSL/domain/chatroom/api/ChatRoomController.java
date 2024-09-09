package com.lawProject.SSL.domain.chatroom.api;

import com.lawProject.SSL.domain.chatroom.exception.ChatRoomException;
import com.lawProject.SSL.domain.chatroom.service.ChatRoomService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.annotation.CurrentUser;
import com.lawProject.SSL.global.common.code.SuccessCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    /* 채팅방 생성 */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createRoom(@CurrentUser User user) {
        String roomId = chatRoomService.createRoom(user);

        URI location = UriComponentsBuilder.newInstance()
                .path("/api/v1/chatroom/{roomId}")
                .buildAndExpand(roomId)
                .toUri();

        // 이때 프론트엔드는 roomId를 뽑아서 해당 채팅방을 "stompClient.subscribe('/sub/chats/' + roomId, function(message)" 처럼 구독해야 됨

        return ApiResponse.onSuccess(SuccessCode._CREATED, location);
    }

    /* 채팅방 삭제 */
    @DeleteMapping("/{roomId}")
    public Mono<ResponseEntity<String>> deleteChatRoom(@PathVariable("roomId") String roomId,
                                                       @CurrentUser User user) {

        return chatRoomService.delete(roomId, user)
                .then(Mono.just(ResponseEntity.ok("AI Response Success")))
                .onErrorResume(ChatRoomException.class, e -> {
                    // ChatRoomException이 발생하면 적절한 에러 응답 반환
                    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Forbidden: " + e.getErrorCode().getMessage()));
                })
                .onErrorResume(Exception.class, e -> {
                    // 기타 예외 처리
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Internal Server Error"));
                });
    }

}
