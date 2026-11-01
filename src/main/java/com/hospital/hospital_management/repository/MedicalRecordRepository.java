package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findByAppointmentId(
            Long appointmentId
    );

    boolean existsByAppointmentId(
            Long appointmentId
    );

    boolean existsByAppointmentIdAndIdNot(
            Long appointmentId,
            Long id
    );

    List<MedicalRecord> findByAppointmentPatientId(
            Long patientId
    );

    List<MedicalRecord> findByAppointmentDoctorId(
            Long doctorId
    );
}