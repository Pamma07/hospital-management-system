package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.MedicalRecordRequestDTO;
import com.hospital.hospital_management.dto.MedicalRecordResponseDTO;
import com.hospital.hospital_management.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(
            MedicalRecordService medicalRecordService) {

        this.medicalRecordService = medicalRecordService;
    }

    // CREATE MEDICAL RECORD
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDTO> createMedicalRecord(
            @RequestBody MedicalRecordRequestDTO request) {

        MedicalRecordResponseDTO response =
                medicalRecordService.createMedicalRecord(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL MEDICAL RECORDS
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDTO>>
    getAllMedicalRecords() {

        return ResponseEntity.ok(
                medicalRecordService.getAllMedicalRecords()
        );
    }

    // GET MEDICAL RECORD BY ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO>
    getMedicalRecordById(@PathVariable Long id) {

        return ResponseEntity.ok(
                medicalRecordService.getMedicalRecordById(id)
        );
    }

    // GET RECORDS BY PATIENT
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponseDTO>>
    getPatientMedicalRecords(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                medicalRecordService.getPatientMedicalRecords(patientId)
        );
    }

    // GET RECORDS BY DOCTOR
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordResponseDTO>>
    getDoctorMedicalRecords(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                medicalRecordService.getDoctorMedicalRecords(doctorId)
        );
    }

    // GET RECORD BY APPOINTMENT
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordResponseDTO>
    getByAppointmentId(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                medicalRecordService.getByAppointmentId(appointmentId)
        );
    }

    // UPDATE MEDICAL RECORD
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO>
    updateMedicalRecord(
            @PathVariable Long id,
            @RequestBody MedicalRecordRequestDTO request) {

        return ResponseEntity.ok(
                medicalRecordService.updateMedicalRecord(
                        id,
                        request
                )
        );
    }

    // DELETE MEDICAL RECORD
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalRecord(
            @PathVariable Long id) {

        medicalRecordService.deleteMedicalRecord(id);

        return ResponseEntity.ok(
                "Medical record deleted successfully"
        );
    }
}