package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.PatientRequestDTO;
import com.hospital.hospital_management.dto.PatientResponseDTO;
import com.hospital.hospital_management.entity.Patient;
import com.hospital.hospital_management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // CREATE patient
    public PatientResponseDTO addPatient(
            PatientRequestDTO request) {

        Patient patient = new Patient();

        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        Patient savedPatient =
                patientRepository.save(patient);

        return convertToResponseDTO(savedPatient);
    }

    public PatientResponseDTO getPatientByUsername(String username){

        Patient patient =
                patientRepository.findByUserUsername(username)
                        .orElseThrow(
                                () -> new RuntimeException("Patient not found")
                        );

        return convertToResponseDTO(patient);
    }

    // GET all patients
    public List<PatientResponseDTO> getAllPatients() {

        return patientRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET patient by ID
    public PatientResponseDTO getPatientById(Long id) {

        return patientRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    // UPDATE patient
    public PatientResponseDTO updatePatient(
            Long id,
            PatientRequestDTO request) {

        Patient patient =
                patientRepository.findById(id)
                        .orElse(null);

        if (patient == null) {
            return null;
        }

        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());

        Patient updatedPatient =
                patientRepository.save(patient);

        return convertToResponseDTO(updatedPatient);
    }

    // DELETE patient
    public boolean deletePatient(Long id) {

        if (!patientRepository.existsById(id)) {
            return false;
        }

        patientRepository.deleteById(id);

        return true;
    }

    // Entity → DTO
    private PatientResponseDTO convertToResponseDTO(
            Patient patient) {

        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress()
        );
    }
}