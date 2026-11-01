package com.hospital.hospital_management.dto;

import com.hospital.hospital_management.entity.ResourceStatus;
import com.hospital.hospital_management.entity.ResourceType;

public class ResourceRequestDTO {

    private String name;

    private ResourceType type;

    private ResourceStatus status;

    private String location;

    private String description;

    public String getName() {
        return name;
    }

    public ResourceType getType() {
        return type;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public void setStatus(ResourceStatus status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}