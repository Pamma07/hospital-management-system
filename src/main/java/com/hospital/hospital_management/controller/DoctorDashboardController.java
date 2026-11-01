package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.DoctorDashboardResponseDTO;
import com.hospital.hospital_management.service.DoctorDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard/doctor")
public class DoctorDashboardController {

    private final DoctorDashboardService doctorDashboardService;

    public DoctorDashboardController(
            DoctorDashboardService doctorDashboardService) {

        this.doctorDashboardService = doctorDashboardService;
    }

    @GetMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDashboardResponseDTO> getDashboard() {

        return ResponseEntity.ok(
                doctorDashboardService.getDashboard()
        );
    }
}