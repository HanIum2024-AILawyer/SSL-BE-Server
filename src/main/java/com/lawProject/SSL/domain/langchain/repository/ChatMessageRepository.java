package com.lawProject.SSL.domain.langchain.repository;

import com.lawProject.SSL.domain.langchain.domain.ChatMessage;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);

}
