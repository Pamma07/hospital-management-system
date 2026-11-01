package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.ResourceAllocationRequestDTO;
import com.hospital.hospital_management.dto.ResourceAllocationResponseDTO;
import com.hospital.hospital_management.service.ResourceAllocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource-allocations")
public class ResourceAllocationController {

    private final ResourceAllocationService allocationService;

    public ResourceAllocationController(
            ResourceAllocationService allocationService) {

        this.allocationService = allocationService;
    }

    // ALLOCATE RESOURCE
    @PostMapping
    public ResponseEntity<ResourceAllocationResponseDTO> allocateResource(
            @RequestBody ResourceAllocationRequestDTO request) {

        ResourceAllocationResponseDTO response =
                allocationService.allocateResource(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL ALLOCATIONS
    @GetMapping
    public ResponseEntity<List<ResourceAllocationResponseDTO>>
    getAllAllocations() {

        return ResponseEntity.ok(
                allocationService.getAllAllocations()
        );
    }

    // GET ALLOCATION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResourceAllocationResponseDTO>
    getAllocationById(@PathVariable Long id) {

        return ResponseEntity.ok(
                allocationService.getAllocationById(id)
        );
    }

    // GET PATIENT ALLOCATIONS
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ResourceAllocationResponseDTO>>
    getPatientAllocations(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                allocationService.getPatientAllocations(patientId)
        );
    }

    // RELEASE RESOURCE
    @PutMapping("/{id}/release")
    public ResponseEntity<ResourceAllocationResponseDTO>
    releaseResource(@PathVariable Long id) {

        return ResponseEntity.ok(
                allocationService.releaseResource(id)
        );
    }
}