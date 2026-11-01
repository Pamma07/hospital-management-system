package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserUsername(String username);

}