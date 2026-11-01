package com.hospital.hospital_management.dto;

public class DashboardResponseDTO {

    private long totalPatients;

    private long totalDoctors;

    private long totalAppointments;

    private long todayAppointments;

    private long confirmedAppointments;

    private long completedAppointments;

    private long totalPrescriptions;

    private long unreadNotifications;

    public DashboardResponseDTO(
            long totalPatients,
            long totalDoctors,
            long totalAppointments,
            long todayAppointments,
            long confirmedAppointments,
            long completedAppointments,
            long totalPrescriptions,
            long unreadNotifications) {

        this.totalPatients = totalPatients;
        this.totalDoctors = totalDoctors;
        this.totalAppointments = totalAppointments;
        this.todayAppointments = todayAppointments;
        this.confirmedAppointments = confirmedAppointments;
        this.completedAppointments = completedAppointments;
        this.totalPrescriptions = totalPrescriptions;
        this.unreadNotifications = unreadNotifications;
    }

    public long getTotalPatients() {
        return totalPatients;
    }

    public long getTotalDoctors() {
        return totalDoctors;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public long getTodayAppointments() {
        return todayAppointments;
    }

    public long getConfirmedAppointments() {
        return confirmedAppointments;
    }

    public long getCompletedAppointments() {
        return completedAppointments;
    }

    public long getTotalPrescriptions() {
        return totalPrescriptions;
    }

    public long getUnreadNotifications() {
        return unreadNotifications;
    }
}