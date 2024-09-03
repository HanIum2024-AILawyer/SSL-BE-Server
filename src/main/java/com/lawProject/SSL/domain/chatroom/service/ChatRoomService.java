package com.lawProject.SSL.domain.chatroom.service;

import com.lawProject.SSL.domain.chatroom.exception.ChatRoomException;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.chatroom.repository.ChatRoomRepository;
import com.lawProject.SSL.domain.langchain.domain.ChatMessage;
import com.lawProject.SSL.domain.langchain.dto.ChatMessageDto;
import com.lawProject.SSL.domain.langchain.service.ChatService;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.common.response.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static com.lawProject.SSL.domain.chatroom.dto.ChatRoomDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserService userService;
    private final ChatService chatService;
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
    public Mono<Void> delete(String roomId, HttpServletRequest request) {
        ChatRoom chatRoom = findByRoomId(roomId);
        User user = userService.getUserInfo(request);

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

    /* 채팅방 열기 메서드 - 채팅방 메시지도 조회 */
    public ChatRoomMessageWithPageInfoResponse openChatRoom(String roomId, int page, int size, User user) {
        Page<ChatMessage> messages =
                chatService.getChatRoomMessages(roomId, page, size);
        PageInfo pageInfo = new PageInfo(page, size, (int) messages.getTotalElements(), messages.getTotalPages());

        //TODO 채팅방 리스트도 반환하도록 수정
//        List<String> chatRoomIdList = user.getChatRoomList().stream()
//                .map(ChatRoom::getRoomId)
//                .toList();

        List<ChatMessageDto.ChatRoomMessageResponse> roomMessageResponses = messages.getContent().stream().map(
                ChatMessageDto.ChatRoomMessageResponse::of
        ).toList();

        return ChatRoomMessageWithPageInfoResponse.of(roomMessageResponses, pageInfo, user.getId());
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
