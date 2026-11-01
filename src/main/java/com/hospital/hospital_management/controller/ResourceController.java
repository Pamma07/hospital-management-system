package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.ResourceRequestDTO;
import com.hospital.hospital_management.dto.ResourceResponseDTO;
import com.hospital.hospital_management.entity.ResourceType;
import com.hospital.hospital_management.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    // CREATE RESOURCE
    @PostMapping
    public ResponseEntity<ResourceResponseDTO> createResource(
            @RequestBody ResourceRequestDTO request) {

        ResourceResponseDTO response =
                resourceService.createResource(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL RESOURCES
    @GetMapping
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources() {

        return ResponseEntity.ok(
                resourceService.getAllResources()
        );
    }

    // GET RESOURCE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> getResourceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                resourceService.getResourceById(id)
        );
    }

    // GET RESOURCES BY TYPE
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ResourceResponseDTO>> getResourcesByType(
            @PathVariable ResourceType type) {

        return ResponseEntity.ok(
                resourceService.getResourcesByType(type)
        );
    }

    // GET AVAILABLE RESOURCES
    @GetMapping("/available")
    public ResponseEntity<List<ResourceResponseDTO>> getAvailableResources() {

        return ResponseEntity.ok(
                resourceService.getAvailableResources()
        );
    }

    // UPDATE RESOURCE
    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> updateResource(
            @PathVariable Long id,
            @RequestBody ResourceRequestDTO request) {

        return ResponseEntity.ok(
                resourceService.updateResource(id, request)
        );
    }

    // DELETE RESOURCE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Long id) {

        resourceService.deleteResource(id);

        return ResponseEntity.noContent().build();
    }
}