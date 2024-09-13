package com.lawProject.SSL.domain.user.model;

import com.lawProject.SSL.domain.chatroom.model.ChatRoom;
import com.lawProject.SSL.domain.inquery.model.InQuery;
import com.lawProject.SSL.domain.lawsuit.model.LawSuit;
import com.lawProject.SSL.global.common.dao.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "tblUser")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "userId", unique = true)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<LawSuit> lawSuitList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<InQuery> inQueryList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    @Builder
    public User(String userId, String name, String provider, String providerId) {
        this.userId = userId;
        this.name = name;
    }

    public void addInQuery(InQuery inQuery) {
        this.inQueryList.add(inQuery);
    }

    public void addLawsuit(LawSuit lawSuit) {
        this.lawSuitList.add(lawSuit);
    }

    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRoomList.add(chatRoom);
    }
}
