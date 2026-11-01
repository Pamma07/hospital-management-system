package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.AuditLogRequestDTO;
import com.hospital.hospital_management.dto.AuditLogResponseDTO;
import com.hospital.hospital_management.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(
            AuditLogService auditLogService) {

        this.auditLogService = auditLogService;
    }

    // CREATE AUDIT LOG
    @PostMapping
    public ResponseEntity<AuditLogResponseDTO>
    createAuditLog(
            @RequestBody AuditLogRequestDTO request) {

        AuditLogResponseDTO response =
                auditLogService.createAuditLog(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL AUDIT LOGS
    @GetMapping
    public ResponseEntity<List<AuditLogResponseDTO>>
    getAllAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllAuditLogs()
        );
    }

    // GET AUDIT LOG BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponseDTO>
    getAuditLogById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogById(id)
        );
    }

    // GET MY AUDIT LOGS
    @GetMapping("/my")
    public ResponseEntity<List<AuditLogResponseDTO>>
    getMyAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getMyAuditLogs()
        );
    }

    // GET AUDIT LOGS BY USERNAME
    @GetMapping("/user/{username}")
    public ResponseEntity<List<AuditLogResponseDTO>>
    getAuditLogsByUsername(
            @PathVariable String username) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByUsername(username)
        );
    }

    // DELETE AUDIT LOG
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteAuditLog(
            @PathVariable Long id) {

        auditLogService.deleteAuditLog(id);

        return ResponseEntity.ok(
                "Audit log deleted successfully"
        );
    }
}