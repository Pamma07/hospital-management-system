package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.DepartmentRequestDTO;
import com.hospital.hospital_management.dto.DepartmentResponseDTO;
import com.hospital.hospital_management.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(
            DepartmentService departmentService) {

        this.departmentService = departmentService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> addDepartment(
            @RequestBody DepartmentRequestDTO request) {

        DepartmentResponseDTO savedDepartment =
                departmentService.addDepartment(request);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Department created successfully",
                        "department",
                        savedDepartment
                )
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<?> getAllDepartments() {

        List<DepartmentResponseDTO> departments =
                departmentService.getAllDepartments();

        if (departments.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of("message", "No departments found")
            );
        }

        return ResponseEntity.ok(departments);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(
            @PathVariable Long id) {

        DepartmentResponseDTO department =
                departmentService.getDepartmentById(id);

        if (department == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Department not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(department);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequestDTO request) {

        DepartmentResponseDTO updatedDepartment =
                departmentService.updateDepartment(id, request);

        if (updatedDepartment == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Department not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Department updated successfully",
                        "department",
                        updatedDepartment
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable Long id) {

        boolean deleted =
                departmentService.deleteDepartment(id);

        if (!deleted) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message",
                            "Department not found with id: " + id
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Department deleted successfully"
                )
        );
    }
}