package com.hospital.hospital_management.dto;

import java.time.LocalDateTime;

public class AuditLogResponseDTO {

    private Long id;

    private String username;

    private String action;

    private String description;

    private LocalDateTime createdAt;

    public AuditLogResponseDTO(
            Long id,
            String username,
            String action,
            String description,
            LocalDateTime createdAt) {

        this.id = id;
        this.username = username;
        this.action = action;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}