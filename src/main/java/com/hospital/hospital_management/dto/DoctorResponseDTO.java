package com.hospital.hospital_management.dto;

public class DoctorResponseDTO {

    private Long id;
    private String username;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private Long departmentId;
    private String departmentName;

    public DoctorResponseDTO() {
    }

    public DoctorResponseDTO(
            Long id,
            String username,
            String name,
            String specialization,
            String phone,
            String email,
            Long departmentId,
            String departmentName) {

        this.id = id;
        this.username = username;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}