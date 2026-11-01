package com.hospital.hospital_management.dto;

import java.time.LocalDateTime;

public class NotificationResponseDTO {

    private Long id;

    private String username;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;

    public NotificationResponseDTO(
            Long id,
            String username,
            String message,
            boolean read,
            LocalDateTime createdAt) {

        this.id = id;
        this.username = username;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}