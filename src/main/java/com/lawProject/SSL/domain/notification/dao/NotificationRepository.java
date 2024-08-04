package com.lawProject.SSL.domain.notification.dao;

import com.lawProject.SSL.domain.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
