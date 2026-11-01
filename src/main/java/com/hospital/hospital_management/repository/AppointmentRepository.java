package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.Appointment;
import com.hospital.hospital_management.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    long count();

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndIdNot(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Long id
    );

    List<Appointment> findByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate
    );

    long countByAppointmentDate(
            LocalDate appointmentDate
    );

    long countByStatus(
            AppointmentStatus status
    );

    long countByDoctorId(Long doctorId);

    long countByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate
    );

    long countByDoctorIdAndStatus(
            Long doctorId,
            AppointmentStatus status
    );

    long countByPatientId(Long patientId);

    long countByPatientIdAndAppointmentDate(
            Long patientId,
            LocalDate appointmentDate
    );

    long countByPatientIdAndStatus(
            Long patientId,
            AppointmentStatus status
    );

    // ============================
    // OWNERSHIP / SECURITY
    // ============================

    List<Appointment> findByPatientUsername(String username);

    List<Appointment> findByDoctorUsername(String username);

    Optional<Appointment> findByIdAndPatientUsername(
            Long id,
            String username
    );

    Optional<Appointment> findByIdAndDoctorUsername(
            Long id,
            String username
    );
}