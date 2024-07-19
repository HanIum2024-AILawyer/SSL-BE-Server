package com.lawProject.SSL.domain.chatmessage.model;

import com.lawProject.SSL.domain.user.model.User;
import com.lawProject.SSL.global.common.dao.BaseEntity;
import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom; // 채팅 방

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 메시지 보낸 사람
}
