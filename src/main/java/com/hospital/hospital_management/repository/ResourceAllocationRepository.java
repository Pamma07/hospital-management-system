package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.ResourceAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceAllocationRepository
        extends JpaRepository<ResourceAllocation, Long> {

    List<ResourceAllocation> findByPatientId(Long patientId);

    List<ResourceAllocation> findByResourceId(Long resourceId);

    boolean existsByResourceIdAndReleasedAtIsNull(Long resourceId);
}