package com.hospital.hospital_management.dto;

import java.time.LocalTime;

public class DoctorScheduleResponseDTO {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public DoctorScheduleResponseDTO() {
    }

    public DoctorScheduleResponseDTO(
            Long id,
            Long doctorId,
            String doctorName,
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime) {

        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}