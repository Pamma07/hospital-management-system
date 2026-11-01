package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {

    long count();

    Optional<Prescription> findByAppointmentId(
            Long appointmentId
    );

    boolean existsByAppointmentId(
            Long appointmentId
    );

    boolean existsByAppointmentIdAndIdNot(
            Long appointmentId,
            Long id
    );

    List<Prescription> findByAppointmentPatientId(
            Long patientId
    );

    List<Prescription> findByAppointmentDoctorId(
            Long doctorId
    );
}