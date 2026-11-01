package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.DoctorRequestDTO;
import com.hospital.hospital_management.dto.DoctorResponseDTO;
import com.hospital.hospital_management.entity.Department;
import com.hospital.hospital_management.entity.Doctor;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.DepartmentRepository;
import com.hospital.hospital_management.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            DepartmentRepository departmentRepository) {

        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    // CREATE doctor
    public DoctorResponseDTO addDoctor(DoctorRequestDTO request) {

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: "
                                        + request.getDepartmentId()
                        )
                );

        Doctor doctor = new Doctor();

        doctor.setUsername(request.getUsername());
        doctor.setUsername(request.getUsername());
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setDepartment(department);

        Doctor savedDoctor = doctorRepository.save(doctor);

        return convertToResponseDTO(savedDoctor);
    }

    // GET CURRENT DOCTOR
    public DoctorResponseDTO getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username)
                .map(this::convertToResponseDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found for username: " + username));
    }

    // GET all doctors
    public List<DoctorResponseDTO> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET doctor by ID
    public DoctorResponseDTO getDoctorById(Long id) {

        return doctorRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    // UPDATE doctor
    public DoctorResponseDTO updateDoctor(
            Long id,
            DoctorRequestDTO request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElse(null);

        if (doctor == null) {
            return null;
        }

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: "
                                        + request.getDepartmentId()
                        )
                );

        doctor.setUsername(request.getUsername());
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setDepartment(department);

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return convertToResponseDTO(updatedDoctor);
    }

    // DELETE doctor
    public boolean deleteDoctor(Long id) {

        if (!doctorRepository.existsById(id)) {
            return false;
        }

        doctorRepository.deleteById(id);

        return true;
    }

    // Entity → Response DTO
    private DoctorResponseDTO convertToResponseDTO(Doctor doctor) {

        Long departmentId = null;
        String departmentName = null;

        if (doctor.getDepartment() != null) {
            departmentId = doctor.getDepartment().getId();
            departmentName = doctor.getDepartment().getName();
        }

        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getUsername(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhone(),
                doctor.getEmail(),
                departmentId,
                departmentName
        );
    }
}