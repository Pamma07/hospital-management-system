package com.hospital.hospital_management.controller;

import jakarta.validation.Valid;
import com.hospital.hospital_management.dto.DoctorRequestDTO;
import com.hospital.hospital_management.dto.DoctorResponseDTO;
import com.hospital.hospital_management.service.DoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // CREATE doctor
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addDoctor(
            @Valid @RequestBody DoctorRequestDTO request) {

        DoctorResponseDTO savedDoctor =
                doctorService.addDoctor(request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Doctor created successfully",
                        "doctor", savedDoctor
                )
        );
    }

    // GET CURRENT DOCTOR PROFILE
    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(doctorService.getDoctorByUsername(authentication.getName()));
    }

    // GET all doctors
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<?> getAllDoctors() {

        List<DoctorResponseDTO> doctors =
                doctorService.getAllDoctors();

        if (doctors.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of("message", "No doctors found")
            );
        }

        return ResponseEntity.ok(doctors);
    }

    // GET doctor by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<?> getDoctorById(
            @PathVariable Long id) {

        DoctorResponseDTO doctor =
                doctorService.getDoctorById(id);

        if (doctor == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Doctor not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(doctor);
    }

    // UPDATE doctor
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDTO request) {

        DoctorResponseDTO updatedDoctor =
                doctorService.updateDoctor(id, request);

        if (updatedDoctor == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Doctor not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message", "Doctor updated successfully",
                        "doctor", updatedDoctor
                )
        );
    }

    // DELETE doctor
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long id) {

        boolean deleted =
                doctorService.deleteDoctor(id);

        if (!deleted) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Doctor not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Doctor deleted successfully"
                )
        );
    }
}