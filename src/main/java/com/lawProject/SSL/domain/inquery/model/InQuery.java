package com.lawProject.SSL.domain.inquery.model;

import com.lawProject.SSL.global.common.dao.BaseEntity;
import com.lawProject.SSL.domain.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@Table(name="inquery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InQuery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquery_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private String answer;

    private boolean isAnswer;

//    @NotNull
//    private InQueryType inQueryType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public InQuery(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.isAnswer = false;
    }

    public boolean getIsAnswer() {
        return this.isAnswer;
    }
}
