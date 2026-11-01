package com.hospital.hospital_management.dto;

public class DoctorDashboardResponseDTO {

    private String doctorName;

    private long totalAppointments;

    private long todayAppointments;

    private long pendingAppointments;

    private long confirmedAppointments;

    private long completedAppointments;

    private long unreadNotifications;

    public DoctorDashboardResponseDTO(
            String doctorName,
            long totalAppointments,
            long todayAppointments,
            long pendingAppointments,
            long confirmedAppointments,
            long completedAppointments,
            long unreadNotifications) {

        this.doctorName = doctorName;
        this.totalAppointments = totalAppointments;
        this.todayAppointments = todayAppointments;
        this.pendingAppointments = pendingAppointments;
        this.confirmedAppointments = confirmedAppointments;
        this.completedAppointments = completedAppointments;
        this.unreadNotifications = unreadNotifications;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public long getTodayAppointments() {
        return todayAppointments;
    }

    public long getPendingAppointments() {
        return pendingAppointments;
    }

    public long getConfirmedAppointments() {
        return confirmedAppointments;
    }

    public long getCompletedAppointments() {
        return completedAppointments;
    }

    public long getUnreadNotifications() {
        return unreadNotifications;
    }
}