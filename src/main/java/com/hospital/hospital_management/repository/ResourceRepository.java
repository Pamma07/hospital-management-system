package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.Resource;
import com.hospital.hospital_management.entity.ResourceStatus;
import com.hospital.hospital_management.entity.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository
        extends JpaRepository<Resource, Long> {

    List<Resource> findByType(ResourceType type);

    List<Resource> findByStatus(ResourceStatus status);

    List<Resource> findByTypeAndStatus(
            ResourceType type,
            ResourceStatus status
    );
}