package com.lawProject.SSL.domain.chatmessage.dao;

import com.lawProject.SSL.domain.chatmessage.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
}
