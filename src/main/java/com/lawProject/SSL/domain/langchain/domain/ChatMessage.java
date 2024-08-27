package com.lawProject.SSL.domain.langchain.domain;

import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.dao.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    private String content; // 메시지 내용

    @Enumerated(EnumType.STRING)
    private SenderType senderType; // USER or AI

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom; // 채팅 방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 메시지 보낸 사람

    public void setUser(User sender) {
        this.user = sender;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    @Builder
    public ChatMessage(String content, User sender, ChatRoom chatRoom, String senderType) {
        this.content = content;
        this.user = sender;
        this.chatRoom = chatRoom;
        this.senderType = resolveSenderType(senderType);
    }

    private SenderType resolveSenderType(String senderType) {
        if ("User".equalsIgnoreCase(senderType)) {
            return SenderType.USER;
        } else {
            return SenderType.AI;
        }
    }
}
