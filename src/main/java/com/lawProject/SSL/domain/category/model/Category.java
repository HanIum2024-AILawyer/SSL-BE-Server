package com.lawProject.SSL.domain.category.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    /**
     * 카테고리를 굳이 따로 엔티티로 빼야할까? 그냥 ChatRoom의 Enum으로 두는 것은?
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

//    private CategoryName categoryName;
}
