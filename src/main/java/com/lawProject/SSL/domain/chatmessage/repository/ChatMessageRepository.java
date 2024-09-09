package com.lawProject.SSL.domain.chatmessage.repository;

import com.lawProject.SSL.domain.chatmessage.model.ChatMessage;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);

    // 주어진 채팅방의 메시지를 lastMessageId 기준으로 페이징하여 조회
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.roomId = :roomId AND cm.id < :messageId ORDER BY cm.id DESC")
    Page<ChatMessage> findMessagesByRoomIdBeforeMessageId(@Param("roomId") String roomId, @Param("messageId") Long messageId, Pageable pageable);

}

