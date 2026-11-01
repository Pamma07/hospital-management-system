package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.DoctorScheduleRequestDTO;
import com.hospital.hospital_management.dto.DoctorScheduleResponseDTO;
import com.hospital.hospital_management.service.DoctorScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    public DoctorScheduleController(
            DoctorScheduleService scheduleService) {

        this.scheduleService = scheduleService;
    }

    // CREATE
    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorScheduleResponseDTO> createSchedule(
            @PathVariable Long doctorId,
            @RequestBody DoctorScheduleRequestDTO request) {

        DoctorScheduleResponseDTO created =
                scheduleService.createSchedule(
                        doctorId,
                        request
                );

        return new ResponseEntity<>(
                created,
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<DoctorScheduleResponseDTO>>
    getAllSchedules() {

        return ResponseEntity.ok(
                scheduleService.getAllSchedules()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponseDTO>
    getScheduleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                scheduleService.getScheduleById(id)
        );
    }

    // GET BY DOCTOR
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorScheduleResponseDTO>>
    getSchedulesByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                scheduleService.getSchedulesByDoctor(doctorId)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponseDTO>
    updateSchedule(
            @PathVariable Long id,
            @RequestBody DoctorScheduleRequestDTO request) {

        return ResponseEntity.ok(
                scheduleService.updateSchedule(
                        id,
                        request
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(
            @PathVariable Long id) {

        scheduleService.deleteSchedule(id);

        return ResponseEntity.ok(
                "Schedule deleted successfully"
        );
    }
}