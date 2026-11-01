package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.PrescriptionRequestDTO;
import com.hospital.hospital_management.dto.PrescriptionResponseDTO;
import com.hospital.hospital_management.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(
            PrescriptionService prescriptionService) {

        this.prescriptionService = prescriptionService;
    }

    // CREATE PRESCRIPTION
    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(
            @RequestBody PrescriptionRequestDTO request) {

        PrescriptionResponseDTO response =
                prescriptionService.createPrescription(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL PRESCRIPTIONS
    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDTO>>
    getAllPrescriptions() {

        return ResponseEntity.ok(
                prescriptionService.getAllPrescriptions()
        );
    }

    // GET PRESCRIPTION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO>
    getPrescriptionById(@PathVariable Long id) {

        return ResponseEntity.ok(
                prescriptionService.getPrescriptionById(id)
        );
    }

    // GET PRESCRIPTION BY APPOINTMENT
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionResponseDTO>
    getByAppointmentId(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                prescriptionService.getByAppointmentId(appointmentId)
        );
    }

    // GET PATIENT PRESCRIPTIONS
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionResponseDTO>>
    getPatientPrescriptions(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                prescriptionService.getPatientPrescriptions(patientId)
        );
    }

    // GET DOCTOR PRESCRIPTIONS
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionResponseDTO>>
    getDoctorPrescriptions(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                prescriptionService.getDoctorPrescriptions(doctorId)
        );
    }

    // UPDATE PRESCRIPTION
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO>
    updatePrescription(
            @PathVariable Long id,
            @RequestBody PrescriptionRequestDTO request) {

        return ResponseEntity.ok(
                prescriptionService.updatePrescription(
                        id,
                        request
                )
        );
    }

    // DELETE PRESCRIPTION
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrescription(
            @PathVariable Long id) {

        prescriptionService.deletePrescription(id);

        return ResponseEntity.ok(
                "Prescription deleted successfully"
        );
    }
}