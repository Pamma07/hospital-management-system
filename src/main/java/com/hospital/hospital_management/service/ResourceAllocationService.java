package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.ResourceAllocationRequestDTO;
import com.hospital.hospital_management.dto.ResourceAllocationResponseDTO;
import com.hospital.hospital_management.entity.Patient;
import com.hospital.hospital_management.entity.Resource;
import com.hospital.hospital_management.entity.ResourceAllocation;
import com.hospital.hospital_management.entity.ResourceStatus;
import com.hospital.hospital_management.exception.AppointmentException;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.PatientRepository;
import com.hospital.hospital_management.repository.ResourceAllocationRepository;
import com.hospital.hospital_management.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResourceAllocationService {

    private final ResourceAllocationRepository allocationRepository;
    private final ResourceRepository resourceRepository;
    private final PatientRepository patientRepository;

    public ResourceAllocationService(
            ResourceAllocationRepository allocationRepository,
            ResourceRepository resourceRepository,
            PatientRepository patientRepository) {

        this.allocationRepository = allocationRepository;
        this.resourceRepository = resourceRepository;
        this.patientRepository = patientRepository;
    }

    // ALLOCATE RESOURCE
    public ResourceAllocationResponseDTO allocateResource(
            ResourceAllocationRequestDTO request) {

        Resource resource =
                resourceRepository.findById(request.getResourceId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource not found with id: "
                                                + request.getResourceId()
                                )
                        );

        Patient patient =
                patientRepository.findById(request.getPatientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found with id: "
                                                + request.getPatientId()
                                )
                        );

        if (resource.getStatus() != ResourceStatus.AVAILABLE) {

            throw new AppointmentException(
                    "Resource is not available for allocation"
            );
        }

        boolean alreadyAllocated =
                allocationRepository
                        .existsByResourceIdAndReleasedAtIsNull(
                                resource.getId()
                        );

        if (alreadyAllocated) {

            throw new AppointmentException(
                    "Resource is already allocated"
            );
        }

        ResourceAllocation allocation =
                new ResourceAllocation();

        allocation.setResource(resource);
        allocation.setPatient(patient);
        allocation.setAllocatedAt(LocalDateTime.now());

        resource.setStatus(ResourceStatus.ALLOCATED);

        resourceRepository.save(resource);

        ResourceAllocation savedAllocation =
                allocationRepository.save(allocation);

        return convertToResponse(savedAllocation);
    }

    // GET ALL ALLOCATIONS
    public List<ResourceAllocationResponseDTO> getAllAllocations() {

        return allocationRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET ALLOCATION BY ID
    public ResourceAllocationResponseDTO getAllocationById(Long id) {

        ResourceAllocation allocation =
                allocationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource allocation not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(allocation);
    }

    // GET PATIENT ALLOCATIONS
    public List<ResourceAllocationResponseDTO> getPatientAllocations(
            Long patientId) {

        return allocationRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // RELEASE RESOURCE
    public ResourceAllocationResponseDTO releaseResource(Long id) {

        ResourceAllocation allocation =
                allocationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource allocation not found with id: "
                                                + id
                                )
                        );

        if (allocation.getReleasedAt() != null) {

            throw new AppointmentException(
                    "Resource has already been released"
            );
        }

        allocation.setReleasedAt(LocalDateTime.now());

        Resource resource =
                allocation.getResource();

        resource.setStatus(ResourceStatus.AVAILABLE);

        resourceRepository.save(resource);

        ResourceAllocation updatedAllocation =
                allocationRepository.save(allocation);

        return convertToResponse(updatedAllocation);
    }

    // ENTITY → DTO
    private ResourceAllocationResponseDTO convertToResponse(
            ResourceAllocation allocation) {

        return new ResourceAllocationResponseDTO(
                allocation.getId(),

                allocation.getResource().getId(),
                allocation.getResource().getName(),

                allocation.getPatient().getId(),
                allocation.getPatient().getName(),

                allocation.getAllocatedAt(),
                allocation.getReleasedAt()
        );
    }
}