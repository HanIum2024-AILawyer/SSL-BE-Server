package com.lawProject.SSL.domain.langchain.service;

import com.lawProject.SSL.domain.langchain.dao.MessageRepository;
import com.lawProject.SSL.domain.langchain.domain.ChatMessage;
import com.lawProject.SSL.domain.langchain.domain.SenderType;
import com.lawProject.SSL.domain.chatroom.application.ChatRoomService;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.user.application.UserService;
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
public class ChatService {
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final MessageRepository messageRepository;

    @Transactional
    public void saveMessage(User user, String message, String roomId, SenderType senderType) {
        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);
        ChatMessage chatMessage;
        if (senderType.equals(SenderType.USER)) {
            chatMessage = ChatMessage.builder()
                    .sender(user)
                    .chatRoom(chatRoom)
                    .content(message)
                    .senderType(String.valueOf(SenderType.USER))
                    .build();
            log.info("사용자가 전송한 메시지 처리");
        } else {
            chatMessage = ChatMessage.builder()
                    .sender(user)
                    .chatRoom(chatRoom)
                    .content(message)
                    .senderType(String.valueOf(SenderType.AI))
                    .build();
            log.info("AI 답변 메시지 처리");
        }

        messageRepository.save(chatMessage);

        chatRoom.addMessage(chatMessage);
    }

    public Page<ChatMessage> getChatRoomMessages(String roomId, int page, int size) {
        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);

        Pageable pageable = PageRequest.of(page-1, size, Sort.by("id").descending());
        Page<ChatMessage> messages = messageRepository.findByChatRoom(pageable, chatRoom);

        return messages;
    }
}