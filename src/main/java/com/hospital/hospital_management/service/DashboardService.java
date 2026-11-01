package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.DashboardResponseDTO;
import com.hospital.hospital_management.entity.AppointmentStatus;
import com.hospital.hospital_management.repository.AppointmentRepository;
import com.hospital.hospital_management.repository.DoctorRepository;
import com.hospital.hospital_management.repository.NotificationRepository;
import com.hospital.hospital_management.repository.PatientRepository;
import com.hospital.hospital_management.repository.PrescriptionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final NotificationRepository notificationRepository;

    public DashboardService(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            PrescriptionRepository prescriptionRepository,
            NotificationRepository notificationRepository) {

        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.notificationRepository = notificationRepository;
    }

    public DashboardResponseDTO getDashboard() {

        long totalPatients =
                patientRepository.count();

        long totalDoctors =
                doctorRepository.count();

        long totalAppointments =
                appointmentRepository.count();

        long todayAppointments =
                appointmentRepository.countByAppointmentDate(
                        LocalDate.now()
                );

        long confirmedAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.CONFIRMED
                );

        long completedAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.COMPLETED
                );

        long totalPrescriptions =
                prescriptionRepository.count();

        String username =
                getLoggedInUsername();

        long unreadNotifications =
                notificationRepository
                        .findByUsernameOrderByCreatedAtDesc(username)
                        .stream()
                        .filter(notification -> !notification.isRead())
                        .count();

        return new DashboardResponseDTO(
                totalPatients,
                totalDoctors,
                totalAppointments,
                todayAppointments,
                confirmedAppointments,
                completedAppointments,
                totalPrescriptions,
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