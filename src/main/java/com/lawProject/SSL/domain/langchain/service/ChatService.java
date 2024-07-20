package com.lawProject.SSL.domain.langchain.service;

import com.lawProject.SSL.domain.chatmessage.dao.MessageRepository;
import com.lawProject.SSL.domain.chatmessage.model.ChatMessage;
import com.lawProject.SSL.domain.chatroom.application.ChatRoomService;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.chatmessage.dto.MessageDto;
import com.lawProject.SSL.domain.user.application.UserService;
import com.lawProject.SSL.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final MessageRepository messageRepository;

    public void saveMessage(MessageDto dto, String roomId) {
        User user = userService.findById(dto.getSenderId());
        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);

        ChatMessage chatMessage = ChatMessage.builder()
                .content(dto.getContent())
                .sender(user)
                .chatRoom(chatRoom)
                .senderType(dto.getSenderType())
                .build();

        messageRepository.save(chatMessage);
        log.info("메시지 저장 완료");
    }

    public Page<ChatMessage> getChatRoomMessages(String roomId, int page, int size) {
        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);

        Pageable pageable = PageRequest.of(page-1, size, Sort.by("id").descending());
        Page<ChatMessage> messages = messageRepository.findByChatRoom(pageable, chatRoom);

        return messages;
    }
}