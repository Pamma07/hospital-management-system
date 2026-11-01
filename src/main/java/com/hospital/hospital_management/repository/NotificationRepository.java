package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    long count();

    List<Notification> findByUsernameOrderByCreatedAtDesc(
            String username
    );
}