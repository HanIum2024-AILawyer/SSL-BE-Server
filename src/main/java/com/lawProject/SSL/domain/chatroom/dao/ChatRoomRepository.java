package com.lawProject.SSL.domain.chatroom.dao;

import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(String roomId);
}
