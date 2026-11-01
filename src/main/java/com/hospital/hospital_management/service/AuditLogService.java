package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.AuditLogRequestDTO;
import com.hospital.hospital_management.dto.AuditLogResponseDTO;
import com.hospital.hospital_management.entity.AuditLog;
import com.hospital.hospital_management.exception.AppointmentException;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.AuditLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository = auditLogRepository;
    }

    // CREATE AUDIT LOG
    public AuditLogResponseDTO createAuditLog(
            AuditLogRequestDTO request) {

        validateRequest(request);

        String username = getLoggedInUsername();

        AuditLog auditLog = new AuditLog(
                username,
                request.getAction(),
                request.getDescription()
        );

        AuditLog savedAuditLog =
                auditLogRepository.save(auditLog);

        return convertToResponse(savedAuditLog);
    }

    // GET ALL AUDIT LOGS
    public List<AuditLogResponseDTO> getAllAuditLogs() {

        return auditLogRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET AUDIT LOG BY ID
    public AuditLogResponseDTO getAuditLogById(
            Long id) {

        AuditLog auditLog =
                auditLogRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Audit log not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(auditLog);
    }

    // GET LOGGED-IN USER AUDIT LOGS
    public List<AuditLogResponseDTO> getMyAuditLogs() {

        String username = getLoggedInUsername();

        return auditLogRepository
                .findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET AUDIT LOGS BY USERNAME
    public List<AuditLogResponseDTO> getAuditLogsByUsername(
            String username) {

        return auditLogRepository
                .findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // DELETE AUDIT LOG
    public void deleteAuditLog(Long id) {

        AuditLog auditLog =
                auditLogRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Audit log not found with id: "
                                                + id
                                )
                        );

        auditLogRepository.delete(auditLog);
    }

    // GET LOGGED-IN USERNAME
    private String getLoggedInUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AppointmentException(
                    "User is not authenticated"
            );
        }

        return authentication.getName();
    }

    // VALIDATION
    private void validateRequest(
            AuditLogRequestDTO request) {

        if (request == null) {
            throw new AppointmentException(
                    "Audit log request is required"
            );
        }

        if (request.getAction() == null ||
                request.getAction().isBlank()) {

            throw new AppointmentException(
                    "Audit action is required"
            );
        }

        if (request.getDescription() == null ||
                request.getDescription().isBlank()) {

            throw new AppointmentException(
                    "Audit description is required"
            );
        }
    }

    // ENTITY → RESPONSE DTO
    private AuditLogResponseDTO convertToResponse(
            AuditLog auditLog) {

        return new AuditLogResponseDTO(
                auditLog.getId(),
                auditLog.getUsername(),
                auditLog.getAction(),
                auditLog.getDescription(),
                auditLog.getCreatedAt()
        );
    }
}