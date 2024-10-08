package com.lawProject.SSL.domain.chatroom.service;

import com.lawProject.SSL.domain.chatroom.exception.ChatRoomException;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.chatroom.repository.ChatRoomRepository;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.lawProject.SSL.domain.chatroom.dto.ChatRoomDto.ChatRoomDeleteRequest;
import static com.lawProject.SSL.domain.chatroom.dto.ChatRoomDto.ChatRoomDeleteResponse;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final WebClient webClient;

    /* 채팅방 생성 메서드 */
    @Transactional
    public String createRoom(User user) {
        String roomId = UUID.randomUUID().toString();

        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .roomId(roomId)
                .build();

        user.addChatRoom(chatRoom);
        chatRoomRepository.save(chatRoom);
        return roomId;
    }

    /* 채팅방 삭제 메서드 */
    @Transactional
    public Mono<Void> delete(String roomId, User user) {
        ChatRoom chatRoom = findByRoomId(roomId);

        if (!chatRoom.getUser().equals(user)) {
            log.error("채팅방 소유자와 현재 로그인 사용자가 일치하지 않습니다.");
            throw new ChatRoomException(ErrorCode._FORBIDDEN);
        }

        chatRoomRepository.delete(chatRoom);
        log.info("DB에서 ChatRoom 삭제, RoomId: {}", roomId);

        return webClient.post()
                .uri("/ai/chat/remove")
                .bodyValue(new ChatRoomDeleteRequest(roomId))
                .retrieve()
                .bodyToMono(ChatRoomDeleteResponse.class)
                .flatMap(response -> {
                    if (response.success()) {
                        return Mono.empty(); // 요청 성공 시 빈 Mono 반환
                    } else {
                        log.error("API 요청 실패");
                        return Mono.error(new ChatRoomException(ErrorCode._INTERNAL_SERVER_ERROR));
                    }
                })
                .doOnError(error -> log.error("Failed to delete chat room from AI server: ", error))
                .then();
    }

    /* Using Method */
    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(
                () -> new ChatRoomException(ErrorCode.ROOM_NOT_FOUND)
        );
    }

    public ChatRoom findByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomException(ErrorCode.ROOM_NOT_FOUND));
    }
}
