package com.hospital.hospital_management.dto;

import com.hospital.hospital_management.entity.ResourceStatus;
import com.hospital.hospital_management.entity.ResourceType;

public class ResourceResponseDTO {

    private Long id;
    private String name;
    private ResourceType type;
    private ResourceStatus status;
    private String location;
    private String description;

    public ResourceResponseDTO(
            Long id,
            String name,
            ResourceType type,
            ResourceStatus status,
            String location,
            String description) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.location = location;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

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
}