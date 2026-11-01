package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.AppointmentRequestDTO;
import com.hospital.hospital_management.dto.AppointmentResponseDTO;
import com.hospital.hospital_management.entity.AppointmentStatus;
import com.hospital.hospital_management.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.hospital_management.dto.AvailableSlotResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService) {

        this.appointmentService = appointmentService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO>
    createAppointment(
            @RequestBody AppointmentRequestDTO request) {

        AppointmentResponseDTO created =
                appointmentService.createAppointment(request);

        return new ResponseEntity<>(
                created,
                HttpStatus.CREATED
        );
    }

    // GET CURRENT PATIENT APPOINTMENTS
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments() {
        return ResponseEntity.ok(appointmentService.getMyAppointments());
    }

    // GET CURRENT DOCTOR APPOINTMENTS
    @GetMapping("/doctor/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyDoctorAppointments() {
        return ResponseEntity.ok(appointmentService.getMyDoctorAppointments());
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>>
    getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO>
    getAppointmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO>
    updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequestDTO request) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(
                        id,
                        request
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.ok(
                "Appointment deleted successfully"
        );
    }
    // UPDATE STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO>
    updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(
                appointmentService.updateAppointmentStatus(
                        id,
                        status
                )
        );
    }

    @GetMapping("/available-slots")
    public AvailableSlotResponseDTO getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam LocalDate date) {

        return appointmentService.getAvailableSlots(
                doctorId,
                date
        );
    }
}