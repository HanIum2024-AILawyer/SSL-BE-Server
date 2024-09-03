package com.lawProject.SSL.domain.langchain.service;

import com.lawProject.SSL.domain.langchain.repository.ChatMessageRepository;
import com.lawProject.SSL.domain.langchain.domain.ChatMessage;
import com.lawProject.SSL.domain.langchain.domain.SenderType;
import com.lawProject.SSL.domain.chatroom.service.ChatRoomService;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.user.service.UserService;
import com.lawProject.SSL.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;

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

    public Page<ChatMessage> getChatRoomMessages(String roomId, int page, int size) {
        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return chatMessageRepository.findByChatRoom(pageable, chatRoom);
    }
}