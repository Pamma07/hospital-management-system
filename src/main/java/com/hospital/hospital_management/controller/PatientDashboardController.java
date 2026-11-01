package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.PatientDashboardResponseDTO;
import com.hospital.hospital_management.service.PatientDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class PatientDashboardController {

    private final PatientDashboardService patientDashboardService;

    public PatientDashboardController(
            PatientDashboardService patientDashboardService) {

        this.patientDashboardService = patientDashboardService;
    }

    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientDashboardResponseDTO> getDashboard() {

        return ResponseEntity.ok(
                patientDashboardService.getDashboard()
        );
    }
}