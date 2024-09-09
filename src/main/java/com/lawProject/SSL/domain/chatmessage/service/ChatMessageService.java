package com.lawProject.SSL.domain.chatmessage.service;

import com.lawProject.SSL.domain.chatmessage.dto.ChatMessageDto;
import com.lawProject.SSL.domain.chatmessage.model.ChatMessage;
import com.lawProject.SSL.domain.chatmessage.model.SenderType;
import com.lawProject.SSL.domain.chatmessage.repository.ChatMessageRepository;
import com.lawProject.SSL.domain.chatroom.exception.ChatRoomException;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.chatroom.service.ChatRoomService;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;

    /* 메시지 저장 메서드 */
    @Transactional
    public void saveMessage(User user, String message, String roomId, SenderType senderType) {
        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);
        ChatMessage chatMessage;
        if (senderType.equals(SenderType.USER)) {
            chatMessage = ChatMessage.builder()
                    .sender(user)
                    .chatRoom(chatRoom)
                    .content(message)
                    .senderType(String.valueOf(senderType))
                    .build();
            log.info("사용자가 전송한 메시지 처리");
        } else {
            chatMessage = ChatMessage.builder()
                    .sender(user)
                    .chatRoom(chatRoom)
                    .content(message)
                    .senderType(String.valueOf(senderType))
                    .build();
            log.info("AI 답변 메시지 처리");
        }

        chatMessageRepository.save(chatMessage);

        chatRoom.addMessage(chatMessage);
    }

    /* 메시지 목록 조회 메서드 */
    public List<ChatMessageDto.ChatRoomMessageResponse> getMessagesForInfiniteScroll(String roomId, Long lastMessageId, int size, User user) {
        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);

        if (!chatRoom.getUser().equals(user)) {
            throw new ChatRoomException(ErrorCode._FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(0, size, Sort.by("id").descending());

        Page<ChatMessage> pagedMessages;
        if (lastMessageId == null) {
            // 처음 로드 시, 최신 메시지부터 시작
            pagedMessages = chatMessageRepository.findMessagesByRoomIdBeforeMessageId(roomId, Long.MAX_VALUE, pageable);
        } else {
            // lastMessageId가 있을 때, 해당 메시지 ID보다 오래된 메시지를 가져오기 위해 페이징 설정
            pagedMessages = chatMessageRepository.findMessagesByRoomIdBeforeMessageId(roomId, lastMessageId, pageable);
        }

        // 빈 페이지일 경우 빈 리스트를 반환
        if (pagedMessages.isEmpty()) {
            return Collections.emptyList();
        }

        // DTO로 변환
        return pagedMessages.stream()
                .map(ChatMessageDto.ChatRoomMessageResponse::of)
                .collect(Collectors.toList());
    }

}