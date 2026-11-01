package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.ResourceRequestDTO;
import com.hospital.hospital_management.dto.ResourceResponseDTO;
import com.hospital.hospital_management.entity.Resource;
import com.hospital.hospital_management.entity.ResourceStatus;
import com.hospital.hospital_management.entity.ResourceType;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    // CREATE
    public ResourceResponseDTO createResource(
            ResourceRequestDTO request) {

        if (request.getName() == null ||
                request.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Resource name is required"
            );
        }

        if (request.getType() == null) {

            throw new IllegalArgumentException(
                    "Resource type is required"
            );
        }

        Resource resource = new Resource();

        resource.setName(request.getName());
        resource.setType(request.getType());

        if (request.getStatus() == null) {
            resource.setStatus(ResourceStatus.AVAILABLE);
        } else {
            resource.setStatus(request.getStatus());
        }

        resource.setLocation(request.getLocation());
        resource.setDescription(request.getDescription());

        Resource savedResource =
                resourceRepository.save(resource);

        return convertToResponse(savedResource);
    }

    // GET ALL
    public List<ResourceResponseDTO> getAllResources() {

        return resourceRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET BY ID
    public ResourceResponseDTO getResourceById(Long id) {

        Resource resource =
                resourceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(resource);
    }

    // GET BY TYPE
    public List<ResourceResponseDTO> getResourcesByType(
            ResourceType type) {

        return resourceRepository
                .findByType(type)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET AVAILABLE
    public List<ResourceResponseDTO> getAvailableResources() {

        return resourceRepository
                .findByStatus(ResourceStatus.AVAILABLE)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // UPDATE
    public ResourceResponseDTO updateResource(
            Long id,
            ResourceRequestDTO request) {

        Resource resource =
                resourceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource not found with id: "
                                                + id
                                )
                        );

        if (request.getName() != null &&
                !request.getName().isBlank()) {

            resource.setName(request.getName());
        }

        if (request.getType() != null) {
            resource.setType(request.getType());
        }

        if (request.getStatus() != null) {
            resource.setStatus(request.getStatus());
        }

        resource.setLocation(request.getLocation());
        resource.setDescription(request.getDescription());

        Resource updatedResource =
                resourceRepository.save(resource);

        return convertToResponse(updatedResource);
    }

    // DELETE
    public void deleteResource(Long id) {

        Resource resource =
                resourceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource not found with id: "
                                                + id
                                )
                        );

        resourceRepository.delete(resource);
    }

    // ENTITY → DTO
    private ResourceResponseDTO convertToResponse(
            Resource resource) {

        return new ResourceResponseDTO(
                resource.getId(),
                resource.getName(),
                resource.getType(),
                resource.getStatus(),
                resource.getLocation(),
                resource.getDescription()
        );
    }
}