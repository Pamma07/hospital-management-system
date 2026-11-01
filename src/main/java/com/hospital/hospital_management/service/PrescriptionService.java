package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.PrescriptionRequestDTO;
import com.hospital.hospital_management.dto.PrescriptionResponseDTO;
import com.hospital.hospital_management.entity.Appointment;
import com.hospital.hospital_management.entity.AppointmentStatus;
import com.hospital.hospital_management.entity.Prescription;
import com.hospital.hospital_management.exception.AppointmentException;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.AppointmentRepository;
import com.hospital.hospital_management.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            AppointmentRepository appointmentRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isDoctor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
    }

    private String username() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }

    private void validateDoctorAccess(Appointment appointment) {
        if (isAdmin()) return;
        if (!isDoctor() || appointment.getDoctor() == null ||
                appointment.getDoctor().getUsername() == null ||
                !username().equals(appointment.getDoctor().getUsername())) {
            throw new AppointmentException("You can only manage prescriptions for your own appointments");
        }
    }

    private void validateReadAccess(Appointment appointment) {
        if (isAdmin()) return;
        if (isDoctor()) {
            if (appointment.getDoctor() == null || appointment.getDoctor().getUsername() == null ||
                    !username().equals(appointment.getDoctor().getUsername())) {
                throw new ResourceNotFoundException("Prescription not found");
            }
        } else {
            if (appointment.getPatient() == null || appointment.getPatient().getUsername() == null ||
                    !username().equals(appointment.getPatient().getUsername())) {
                throw new ResourceNotFoundException("Prescription not found");
            }
        }
    }

    // CREATE PRESCRIPTION
    public PrescriptionResponseDTO createPrescription(
            PrescriptionRequestDTO request) {

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
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new AppointmentException(
                    "Prescription can only be created for a completed appointment"
            );
        }

        validateDoctorAccess(appointment);

        boolean alreadyExists =
                prescriptionRepository
                        .existsByAppointmentId(
                                appointment.getId()
                        );

        if (alreadyExists) {

            throw new AppointmentException(
                    "A prescription already exists for this appointment"
            );
        }

        Prescription prescription =
                new Prescription();

        prescription.setAppointment(appointment);
        prescription.setDiagnosis(request.getDiagnosis());
        prescription.setMedicines(request.getMedicines());
        prescription.setInstructions(request.getInstructions());
        prescription.setCreatedAt(LocalDateTime.now());

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        return convertToResponse(savedPrescription);
    }

    // GET PRESCRIPTION BY ID
    public PrescriptionResponseDTO getPrescriptionById(
            Long id) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found with id: "
                                                + id
                                )
                        );

        validateReadAccess(prescription.getAppointment());
        return convertToResponse(prescription);
    }

    // GET PRESCRIPTION BY APPOINTMENT
    public PrescriptionResponseDTO getByAppointmentId(
            Long appointmentId) {

        Prescription prescription =
                prescriptionRepository
                        .findByAppointmentId(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found for appointment id: "
                                                + appointmentId
                                )
                        );

        validateReadAccess(prescription.getAppointment());
        return convertToResponse(prescription);
    }

    // GET PATIENT PRESCRIPTIONS
    public List<PrescriptionResponseDTO> getPatientPrescriptions(
            Long patientId) {

        return prescriptionRepository
                .findByAppointmentPatientId(patientId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET DOCTOR PRESCRIPTIONS
    public List<PrescriptionResponseDTO> getDoctorPrescriptions(
            Long doctorId) {

        return prescriptionRepository
                .findByAppointmentDoctorId(doctorId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET ALL PRESCRIPTIONS
    public List<PrescriptionResponseDTO> getAllPrescriptions() {

        return prescriptionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // VALIDATION
    private void validateRequest(
            PrescriptionRequestDTO request) {

        if (request.getAppointmentId() == null) {

            throw new AppointmentException(
                    "Appointment is required"
            );
        }

        if (request.getDiagnosis() == null ||
                request.getDiagnosis().isBlank()) {

            throw new AppointmentException(
                    "Diagnosis is required"
            );
        }

        if (request.getMedicines() == null ||
                request.getMedicines().isBlank()) {

            throw new AppointmentException(
                    "Medicines are required"
            );
        }
    }

    // ENTITY → DTO
    private PrescriptionResponseDTO convertToResponse(
            Prescription prescription) {

        Appointment appointment =
                prescription.getAppointment();

        return new PrescriptionResponseDTO(
                prescription.getId(),

                appointment.getId(),

                appointment.getPatient().getId(),
                appointment.getPatient().getName(),

                appointment.getDoctor().getId(),
                appointment.getDoctor().getName(),

                prescription.getDiagnosis(),
                prescription.getMedicines(),
                prescription.getInstructions(),

                prescription.getCreatedAt()
        );
    }

    // UPDATE PRESCRIPTION
    public PrescriptionResponseDTO updatePrescription(
            Long id,
            PrescriptionRequestDTO request) {

        validateRequest(request);

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found with id: "
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
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new AppointmentException(
                    "Prescription can only be created or updated for a completed appointment"
            );
        }

        validateDoctorAccess(appointment);

        // Check if another prescription already belongs to this appointment
        boolean alreadyExists =
                prescriptionRepository
                        .existsByAppointmentIdAndIdNot(
                                appointment.getId(),
                                id
                        );

        if (alreadyExists) {

            throw new AppointmentException(
                    "A prescription already exists for this appointment"
            );
        }

        prescription.setAppointment(appointment);
        prescription.setDiagnosis(request.getDiagnosis());
        prescription.setMedicines(request.getMedicines());
        prescription.setInstructions(request.getInstructions());

        // Keep the original createdAt
        Prescription updatedPrescription =
                prescriptionRepository.save(prescription);

        return convertToResponse(updatedPrescription);
    }

    // DELETE PRESCRIPTION
    public void deletePrescription(Long id) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found with id: "
                                                + id
                                )
                        );

        prescriptionRepository.delete(prescription);
    }
}