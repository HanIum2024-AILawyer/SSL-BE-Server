package com.lawProject.SSL.domain.langchain.dao;

import com.lawProject.SSL.domain.langchain.domain.ChatMessage;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);

}
