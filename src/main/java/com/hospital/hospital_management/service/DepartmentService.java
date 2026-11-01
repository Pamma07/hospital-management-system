package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.DepartmentRequestDTO;
import com.hospital.hospital_management.dto.DepartmentResponseDTO;
import com.hospital.hospital_management.entity.Department;
import com.hospital.hospital_management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository) {

        this.departmentRepository = departmentRepository;
    }

    // CREATE
    public DepartmentResponseDTO addDepartment(
            DepartmentRequestDTO request) {

        Department department = new Department();

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        Department savedDepartment =
                departmentRepository.save(department);

        return convertToResponseDTO(savedDepartment);
    }

    // GET ALL
    public List<DepartmentResponseDTO> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET BY ID
    public DepartmentResponseDTO getDepartmentById(Long id) {

        return departmentRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    // UPDATE
    public DepartmentResponseDTO updateDepartment(
            Long id,
            DepartmentRequestDTO request) {

        Department department =
                departmentRepository.findById(id)
                        .orElse(null);

        if (department == null) {
            return null;
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        Department updatedDepartment =
                departmentRepository.save(department);

        return convertToResponseDTO(updatedDepartment);
    }

    // DELETE
    public boolean deleteDepartment(Long id) {

        if (!departmentRepository.existsById(id)) {
            return false;
        }

        departmentRepository.deleteById(id);

        return true;
    }

    // Entity → DTO
    private DepartmentResponseDTO convertToResponseDTO(
            Department department) {

        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getDescription()
        );
    }
}