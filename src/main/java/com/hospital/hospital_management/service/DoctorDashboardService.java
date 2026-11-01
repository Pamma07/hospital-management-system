package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.DoctorDashboardResponseDTO;
import com.hospital.hospital_management.entity.AppointmentStatus;
import com.hospital.hospital_management.entity.Doctor;
import com.hospital.hospital_management.repository.AppointmentRepository;
import com.hospital.hospital_management.repository.DoctorRepository;
import com.hospital.hospital_management.repository.NotificationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DoctorDashboardService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;

    public DoctorDashboardService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            NotificationRepository notificationRepository) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.notificationRepository = notificationRepository;
    }

    public DoctorDashboardResponseDTO getDashboard() {

        String username = getLoggedInUsername();

        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor profile not found"
                        )
                );

        Long doctorId = doctor.getId();

        long totalAppointments =
                appointmentRepository.countByDoctorId(doctorId);

        long todayAppointments =
                appointmentRepository
                        .countByDoctorIdAndAppointmentDate(
                                doctorId,
                                LocalDate.now()
                        );

        long pendingAppointments =
                appointmentRepository
                        .countByDoctorIdAndStatus(
                                doctorId,
                                AppointmentStatus.PENDING
                        );

        long confirmedAppointments =
                appointmentRepository
                        .countByDoctorIdAndStatus(
                                doctorId,
                                AppointmentStatus.CONFIRMED
                        );

        long completedAppointments =
                appointmentRepository
                        .countByDoctorIdAndStatus(
                                doctorId,
                                AppointmentStatus.COMPLETED
                        );

        long unreadNotifications =
                notificationRepository
                        .findByUsernameOrderByCreatedAtDesc(username)
                        .stream()
                        .filter(notification -> !notification.isRead())
                        .count();

        return new DoctorDashboardResponseDTO(
                doctor.getName(),
                totalAppointments,
                todayAppointments,
                pendingAppointments,
                confirmedAppointments,
                completedAppointments,
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