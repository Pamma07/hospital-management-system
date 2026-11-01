package com.hospital.hospital_management.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AvailableSlotResponseDTO {

    private Long doctorId;
    private String doctorName;
    private LocalDate date;
    private List<LocalTime> availableSlots;

    public AvailableSlotResponseDTO(
            Long doctorId,
            String doctorName,
            LocalDate date,
            List<LocalTime> availableSlots) {

        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.availableSlots = availableSlots;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<LocalTime> getAvailableSlots() {
        return availableSlots;
    }
}