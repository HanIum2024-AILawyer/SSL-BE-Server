package com.lawProject.SSL.domain.image.model;

import com.lawProject.SSL.domain.Lawyer.model.Lawyer;
import com.lawProject.SSL.domain.notification.model.Notification;
import com.lawProject.SSL.domain.user.model.User;
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

    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lawyer_id")
    private Lawyer lawyer;

//    public static Image of(..., String imageUrl){
//
//    }
}
