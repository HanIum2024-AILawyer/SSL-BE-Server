package com.lawProject.SSL.domain.image.model;

import com.lawProject.SSL.domain.inquery.model.InQuery;
import com.lawProject.SSL.domain.lawyer.model.Lawyer;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String imageName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "inquery_id")
    private InQuery inQuery;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "lawyer_id")
    private Lawyer lawyer;

    /* 연관관계 메서드 */
    public static Image ofLawyer(Lawyer lawyer, String imageName){
        return Image.builder()
                .imageName(imageName)
                .lawyer(lawyer)
                .build();
    }
}
