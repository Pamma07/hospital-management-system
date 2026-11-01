package com.hospital.hospital_management.dto;

import java.time.LocalDateTime;

public class ResourceAllocationResponseDTO {

    private Long id;

    private Long resourceId;
    private String resourceName;

    private Long patientId;
    private String patientName;

    private LocalDateTime allocatedAt;
    private LocalDateTime releasedAt;

    public ResourceAllocationResponseDTO(
            Long id,
            Long resourceId,
            String resourceName,
            Long patientId,
            String patientName,
            LocalDateTime allocatedAt,
            LocalDateTime releasedAt) {

        this.id = id;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.allocatedAt = allocatedAt;
        this.releasedAt = releasedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public LocalDateTime getAllocatedAt() {
        return allocatedAt;
    }

    public LocalDateTime getReleasedAt() {
        return releasedAt;
    }
}