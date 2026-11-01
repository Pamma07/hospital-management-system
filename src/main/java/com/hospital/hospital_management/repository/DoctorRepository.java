package com.hospital.hospital_management.repository;

import com.hospital.hospital_management.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    long count();

    Optional<Doctor> findByUsername(String username);
}