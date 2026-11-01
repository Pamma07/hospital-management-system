package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.PatientRequestDTO;
import com.hospital.hospital_management.dto.PatientResponseDTO;
import com.hospital.hospital_management.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // CREATE patient
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPatient(
            @RequestBody PatientRequestDTO request) {

        PatientResponseDTO savedPatient =
                patientService.addPatient(request);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Patient created successfully",
                        "patient",
                        savedPatient
                )
        );
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> getMyProfile(Authentication authentication){

        String username = authentication.getName();

        PatientResponseDTO patient =
                patientService.getPatientByUsername(username);

        return ResponseEntity.ok(patient);
    }

    // GET all patients
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> getAllPatients() {

        List<PatientResponseDTO> patients =
                patientService.getAllPatients();

        if (patients.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of("message", "No patients found")
            );
        }

        return ResponseEntity.ok(patients);
    }

    // GET patient by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> getPatientById(
            @PathVariable Long id) {

        PatientResponseDTO patient =
                patientService.getPatientById(id);

        if (patient == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Patient not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(patient);
    }

    // UPDATE patient
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientRequestDTO request) {

        PatientResponseDTO updatedPatient =
                patientService.updatePatient(id, request);

        if (updatedPatient == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Patient not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Patient updated successfully",
                        "patient",
                        updatedPatient
                )
        );
    }

    // DELETE patient
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePatient(
            @PathVariable Long id) {

        boolean deleted =
                patientService.deletePatient(id);

        if (!deleted) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Patient not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Patient deleted successfully"
                )
        );
    }
}