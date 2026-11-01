package com.hospital.hospital_management.dto;

public class PatientDashboardResponseDTO {

    private String patientName;
    private long totalAppointments;
    private long todayAppointments;
    private long pendingAppointments;
    private long confirmedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
    private long unreadNotifications;

    public PatientDashboardResponseDTO(
            String patientName,
            long totalAppointments,
            long todayAppointments,
            long pendingAppointments,
            long confirmedAppointments,
            long completedAppointments,
            long cancelledAppointments,
            long unreadNotifications) {

        this.patientName = patientName;
        this.totalAppointments = totalAppointments;
        this.todayAppointments = todayAppointments;
        this.pendingAppointments = pendingAppointments;
        this.confirmedAppointments = confirmedAppointments;
        this.completedAppointments = completedAppointments;
        this.cancelledAppointments = cancelledAppointments;
        this.unreadNotifications = unreadNotifications;
    }

    public String getPatientName() {
        return patientName;
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

    public long getCancelledAppointments() {
        return cancelledAppointments;
    }

    public long getUnreadNotifications() {
        return unreadNotifications;
    }
}