package com.lawProject.SSL.domain.chatroom.application;

import com.lawProject.SSL.domain.chatroom.dao.ChatRoomRepository;
import com.lawProject.SSL.domain.chatroom.exception.ChatRoomException;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.global.common.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

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
