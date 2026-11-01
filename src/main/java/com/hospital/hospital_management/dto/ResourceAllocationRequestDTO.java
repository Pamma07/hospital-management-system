package com.hospital.hospital_management.dto;

public class ResourceAllocationRequestDTO {

    private Long resourceId;

    private Long patientId;

    public Long getResourceId() {
        return resourceId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}