package com.hospital.hospital_management.dto;

public class MedicalRecordRequestDTO {

    private Long appointmentId;

    private String symptoms;

    private String diagnosis;

    private String treatment;

    private String notes;

    public MedicalRecordRequestDTO() {
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}