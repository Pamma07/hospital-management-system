package com.hospital.hospital_management.dto;

public class AuditLogRequestDTO {

    private String action;

    private String description;

    public AuditLogRequestDTO() {
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}