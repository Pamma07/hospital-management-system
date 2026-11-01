package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.MedicalRecordRequestDTO;
import com.hospital.hospital_management.dto.MedicalRecordResponseDTO;
import com.hospital.hospital_management.entity.Appointment;
import com.hospital.hospital_management.entity.AppointmentStatus;
import com.hospital.hospital_management.entity.Doctor;
import com.hospital.hospital_management.entity.MedicalRecord;
import com.hospital.hospital_management.entity.Patient;
import com.hospital.hospital_management.exception.AppointmentException;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.AppointmentRepository;
import com.hospital.hospital_management.repository.MedicalRecordRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;

    public MedicalRecordService(
            MedicalRecordRepository medicalRecordRepository,
            AppointmentRepository appointmentRepository) {

        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
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

    // VALIDATE DOCTOR OWNERSHIP
    private void validateDoctorOwnership(
            Appointment appointment) {

        String username = getLoggedInUsername();

        Doctor doctor = appointment.getDoctor();

        if (doctor == null ||
                !username.equals(doctor.getUsername())) {

            throw new AppointmentException(
                    "You can only manage medical records for your own appointments"
            );
        }
    }

    // VALIDATE PATIENT OWNERSHIP
    private void validatePatientOwnership(
            Patient patient) {

        String username = getLoggedInUsername();

        if (patient == null ||
                !username.equals(patient.getUsername())) {

            throw new AppointmentException(
                    "You can only access your own medical records"
            );
        }
    }

    // CREATE MEDICAL RECORD
    public MedicalRecordResponseDTO createMedicalRecord(
            MedicalRecordRequestDTO request) {

        validateRequest(request);

        Appointment appointment =
                appointmentRepository.findById(
                        request.getAppointmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: "
                                        + request.getAppointmentId()
                        )
                );

        // Only completed appointments can have medical records
        if (appointment.getStatus() !=
                AppointmentStatus.COMPLETED) {

            throw new AppointmentException(
                    "Medical record can only be created for a completed appointment"
            );
        }

        // Doctor can create record only for own appointment
        validateDoctorOwnership(appointment);

        // Prevent duplicate medical record
        if (medicalRecordRepository.existsByAppointmentId(
                appointment.getId())) {

            throw new AppointmentException(
                    "A medical record already exists for this appointment"
            );
        }

        MedicalRecord medicalRecord =
                new MedicalRecord();

        medicalRecord.setAppointment(appointment);
        medicalRecord.setSymptoms(request.getSymptoms());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setNotes(request.getNotes());
        medicalRecord.setCreatedAt(LocalDateTime.now());

        MedicalRecord savedRecord =
                medicalRecordRepository.save(medicalRecord);

        return convertToResponse(savedRecord);
    }

    // GET BY ID
    public MedicalRecordResponseDTO getMedicalRecordById(
            Long id) {

        MedicalRecord record =
                medicalRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical record not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(record);
    }

    // GET BY APPOINTMENT
    public MedicalRecordResponseDTO getByAppointmentId(
            Long appointmentId) {

        MedicalRecord record =
                medicalRecordRepository
                        .findByAppointmentId(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical record not found for appointment id: "
                                                + appointmentId
                                )
                        );

        return convertToResponse(record);
    }

    // GET PATIENT RECORDS
    public List<MedicalRecordResponseDTO>
    getPatientMedicalRecords(Long patientId) {

        return medicalRecordRepository
                .findByAppointmentPatientId(patientId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET DOCTOR RECORDS
    public List<MedicalRecordResponseDTO>
    getDoctorMedicalRecords(Long doctorId) {

        return medicalRecordRepository
                .findByAppointmentDoctorId(doctorId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET ALL
    public List<MedicalRecordResponseDTO>
    getAllMedicalRecords() {

        return medicalRecordRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // UPDATE
    public MedicalRecordResponseDTO updateMedicalRecord(
            Long id,
            MedicalRecordRequestDTO request) {

        validateRequest(request);

        MedicalRecord record =
                medicalRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical record not found with id: "
                                                + id
                                )
                        );

        Appointment appointment =
                appointmentRepository.findById(
                        request.getAppointmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: "
                                        + request.getAppointmentId()
                        )
                );

        if (appointment.getStatus() !=
                AppointmentStatus.COMPLETED) {

            throw new AppointmentException(
                    "Medical record can only be created or updated for a completed appointment"
            );
        }

        // Doctor can update only their own appointment record
        validateDoctorOwnership(appointment);

        boolean anotherRecordExists =
                medicalRecordRepository
                        .existsByAppointmentIdAndIdNot(
                                appointment.getId(),
                                id
                        );

        if (anotherRecordExists) {

            throw new AppointmentException(
                    "A medical record already exists for this appointment"
            );
        }

        record.setAppointment(appointment);
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setTreatment(request.getTreatment());
        record.setNotes(request.getNotes());

        MedicalRecord updatedRecord =
                medicalRecordRepository.save(record);

        return convertToResponse(updatedRecord);
    }

    // DELETE
    public void deleteMedicalRecord(Long id) {

        MedicalRecord record =
                medicalRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical record not found with id: "
                                                + id
                                )
                        );

        medicalRecordRepository.delete(record);
    }

    // VALIDATION
    private void validateRequest(
            MedicalRecordRequestDTO request) {

        if (request == null) {

            throw new AppointmentException(
                    "Medical record request is required"
            );
        }

        if (request.getAppointmentId() == null) {

            throw new AppointmentException(
                    "Appointment is required"
            );
        }

        if (request.getSymptoms() == null ||
                request.getSymptoms().isBlank()) {

            throw new AppointmentException(
                    "Symptoms are required"
            );
        }

        if (request.getDiagnosis() == null ||
                request.getDiagnosis().isBlank()) {

            throw new AppointmentException(
                    "Diagnosis is required"
            );
        }
    }

    // ENTITY → DTO
    private MedicalRecordResponseDTO convertToResponse(
            MedicalRecord record) {

        Appointment appointment =
                record.getAppointment();

        Patient patient =
                appointment.getPatient();

        Doctor doctor =
                appointment.getDoctor();

        return new MedicalRecordResponseDTO(
                record.getId(),

                appointment.getId(),

                patient.getId(),
                patient.getName(),

                doctor.getId(),
                doctor.getName(),

                record.getSymptoms(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getNotes(),

                record.getCreatedAt()
        );
    }
}