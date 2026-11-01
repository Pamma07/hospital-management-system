package com.hospital.hospital_management.dto;

public class PrescriptionRequestDTO {

    private Long appointmentId;

    private String diagnosis;

    private String medicines;

    private String instructions;

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getMedicines() {
        return medicines;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}