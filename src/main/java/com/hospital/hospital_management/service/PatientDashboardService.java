package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.PatientDashboardResponseDTO;
import com.hospital.hospital_management.entity.AppointmentStatus;
import com.hospital.hospital_management.entity.Patient;
import com.hospital.hospital_management.repository.AppointmentRepository;
import com.hospital.hospital_management.repository.NotificationRepository;
import com.hospital.hospital_management.repository.PatientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PatientDashboardService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;

    public PatientDashboardService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            NotificationRepository notificationRepository) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.notificationRepository = notificationRepository;
    }

    public PatientDashboardResponseDTO getDashboard() {

        String username = getLoggedInUsername();

        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found")
                );

        Long patientId = patient.getId();

        long totalAppointments =
                appointmentRepository.countByPatientId(patientId);

        long todayAppointments =
                appointmentRepository.countByPatientIdAndAppointmentDate(
                        patientId,
                        LocalDate.now()
                );

        long pendingAppointments =
                appointmentRepository.countByPatientIdAndStatus(
                        patientId,
                        AppointmentStatus.PENDING
                );

        long confirmedAppointments =
                appointmentRepository.countByPatientIdAndStatus(
                        patientId,
                        AppointmentStatus.CONFIRMED
                );

        long completedAppointments =
                appointmentRepository.countByPatientIdAndStatus(
                        patientId,
                        AppointmentStatus.COMPLETED
                );

        long cancelledAppointments =
                appointmentRepository.countByPatientIdAndStatus(
                        patientId,
                        AppointmentStatus.CANCELLED
                );

        long unreadNotifications =
                notificationRepository
                        .findByUsernameOrderByCreatedAtDesc(username)
                        .stream()
                        .filter(notification -> !notification.isRead())
                        .count();

        return new PatientDashboardResponseDTO(
                patient.getName(),
                totalAppointments,
                todayAppointments,
                pendingAppointments,
                confirmedAppointments,
                completedAppointments,
                cancelledAppointments,
                unreadNotifications
        );
    }

    private String getLoggedInUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        return authentication.getName();
    }
}