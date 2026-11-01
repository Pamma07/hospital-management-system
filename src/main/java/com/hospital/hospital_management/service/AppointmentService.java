package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.AvailableSlotResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.hospital.hospital_management.entity.DoctorSchedule;
import com.hospital.hospital_management.entity.Appointment;
import com.hospital.hospital_management.entity.AppointmentStatus;
import com.hospital.hospital_management.entity.Doctor;
import com.hospital.hospital_management.dto.AppointmentRequestDTO;
import com.hospital.hospital_management.dto.AppointmentResponseDTO;
import com.hospital.hospital_management.entity.*;
import com.hospital.hospital_management.exception.AppointmentException;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.AppointmentRepository;
import com.hospital.hospital_management.repository.DoctorRepository;
import com.hospital.hospital_management.repository.DoctorScheduleRepository;
import com.hospital.hospital_management.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorScheduleRepository doctorScheduleRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.appointmentRepository = appointmentRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

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

    private void validatePatientOwnership(Patient patient) {

        String loggedInUsername = getLoggedInUsername();

        if (patient == null || patient.getUsername() == null ||
                !patient.getUsername().equals(loggedInUsername)) {

            throw new AppointmentException(
                    "You can only manage your own appointments"
            );
        }
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    private void validateAppointmentReadAccess(Appointment appointment) {
        if (hasRole("ADMIN")) {
            return;
        }
        String username = getLoggedInUsername();
        if (hasRole("DOCTOR")) {
            if (appointment.getDoctor() == null ||
                    appointment.getDoctor().getUsername() == null ||
                    !username.equals(appointment.getDoctor().getUsername())) {
                throw new ResourceNotFoundException("Appointment not found");
            }
            return;
        }
        if (hasRole("PATIENT")) {
            if (appointment.getPatient() == null ||
                    appointment.getPatient().getUsername() == null ||
                    !username.equals(appointment.getPatient().getUsername())) {
                throw new ResourceNotFoundException("Appointment not found");
            }
            return;
        }
        throw new AppointmentException("You are not allowed to access appointments");
    }

    // CREATE
    public AppointmentResponseDTO createAppointment(
            AppointmentRequestDTO request) {

        validateRequest(request);

        Patient patient = patientRepository.findById(
                request.getPatientId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Patient not found with id: "
                                + request.getPatientId()
                )
        );
        validatePatientOwnership(patient);

        Doctor doctor = doctorRepository.findById(
                request.getDoctorId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Doctor not found with id: "
                                + request.getDoctorId()
                )
        );

        validateDoctorSchedule(
                doctor.getId(),
                request.getAppointmentDate(),
                request.getAppointmentTime()
        );

        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                doctor.getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        );

        if (alreadyBooked) {
            throw new AppointmentException(
                    "This time slot is already booked"
            );
        }

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );
        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return convertToResponseDTO(savedAppointment);
    }

    // GET CURRENT PATIENT APPOINTMENTS
    public List<AppointmentResponseDTO> getMyAppointments() {
        String username = getLoggedInUsername();
        return appointmentRepository.findByPatientUsername(username)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET CURRENT DOCTOR APPOINTMENTS
    public List<AppointmentResponseDTO> getMyDoctorAppointments() {
        String username = getLoggedInUsername();
        return appointmentRepository.findByDoctorUsername(username)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET ALL
    public List<AppointmentResponseDTO> getAllAppointments() {

        if (hasRole("DOCTOR")) {
            return getMyDoctorAppointments();
        }

        return appointmentRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET BY ID
    public AppointmentResponseDTO getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id
                        )
                );

        validateAppointmentReadAccess(appointment);
        return convertToResponseDTO(appointment);
    }

    // UPDATE
    public AppointmentResponseDTO updateAppointment(
            Long id,
            AppointmentRequestDTO request) {

        validateRequest(request);

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );

        validatePatientOwnership(appointment.getPatient());

        Patient patient = patientRepository.findById(
                request.getPatientId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Patient not found with id: "
                                + request.getPatientId()
                )
        );
        validatePatientOwnership(patient);

        Doctor doctor = doctorRepository.findById(
                request.getDoctorId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Doctor not found with id: "
                                + request.getDoctorId()
                )
        );

        validateDoctorSchedule(
                doctor.getId(),
                request.getAppointmentDate(),
                request.getAppointmentTime()
        );

        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndIdNot(
                                doctor.getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                id
                        );

        if (alreadyBooked) {
            throw new AppointmentException(
                    "This time slot is already booked"
            );
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );
        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );
        appointment.setReason(request.getReason());

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        return convertToResponseDTO(updatedAppointment);
    }

    // DELETE
    public void deleteAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );
        validatePatientOwnership(appointment.getPatient());

        appointmentRepository.delete(appointment);
    }

    // UPDATE APPOINTMENT STATUS
    public AppointmentResponseDTO updateAppointmentStatus(
            Long id,
            AppointmentStatus status) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: " + id
                                )
                        );

        if (status == null) {
            throw new AppointmentException(
                    "Appointment status is required"
            );
        }

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));

        if (isDoctor) {
            if (appointment.getDoctor() == null ||
                    !getLoggedInUsername().equals(appointment.getDoctor().getUsername())) {
                throw new AppointmentException(
                        "You can only update status for your own appointments"
                );
            }
        } else if (!isAdmin) {
            throw new AppointmentException("You are not allowed to update appointment status");
        }

        appointment.setStatus(status);

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        return convertToResponseDTO(updatedAppointment);
    }

    // VALIDATE REQUEST
    private void validateRequest(
            AppointmentRequestDTO request) {

        if (request.getPatientId() == null) {
            throw new AppointmentException(
                    "Patient is required"
            );
        }

        if (request.getDoctorId() == null) {
            throw new AppointmentException(
                    "Doctor is required"
            );
        }

        if (request.getAppointmentDate() == null) {
            throw new AppointmentException(
                    "Appointment date is required"
            );
        }

        if (request.getAppointmentTime() == null) {
            throw new AppointmentException(
                    "Appointment time is required"
            );
        }

        if (request.getAppointmentDate()
                .isBefore(LocalDate.now())) {

            throw new AppointmentException(
                    "Appointment date cannot be in the past"
            );
        }
    }

    // CHECK DOCTOR SCHEDULE
    private void validateDoctorSchedule(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime) {

        DayOfWeek dayOfWeek =
                appointmentDate.getDayOfWeek();

        String dayName = dayOfWeek.toString();

        List<DoctorSchedule> schedules =
                doctorScheduleRepository
                        .findByDoctorIdAndDayOfWeek(
                                doctorId,
                                dayName
                        );

        if (schedules.isEmpty()) {
            throw new AppointmentException(
                    "Doctor is not available on " + dayName
            );
        }

        boolean withinSchedule = schedules.stream()
                .anyMatch(schedule ->
                        !appointmentTime.isBefore(
                                schedule.getStartTime()
                        )
                                &&
                                appointmentTime.isBefore(
                                        schedule.getEndTime()
                                )
                );

        if (!withinSchedule) {
            throw new AppointmentException(
                    "Doctor is not available at "
                            + appointmentTime
            );
        }
    }

    // ENTITY → RESPONSE DTO
    private AppointmentResponseDTO convertToResponseDTO(
            Appointment appointment) {

        Long patientId = null;
        String patientName = null;

        Long doctorId = null;
        String doctorName = null;

        if (appointment.getPatient() != null) {
            patientId = appointment.getPatient().getId();
            patientName = appointment.getPatient().getName();
        }

        if (appointment.getDoctor() != null) {
            doctorId = appointment.getDoctor().getId();
            doctorName = appointment.getDoctor().getName();
        }

        return new AppointmentResponseDTO(
                appointment.getId(),
                patientId,
                patientName,
                doctorId,
                doctorName,
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getReason(),
                appointment.getStatus()
        );
    }
    public AvailableSlotResponseDTO getAvailableSlots(
            Long doctorId,
            LocalDate date) {

        if (doctorId == null || date == null) {
            throw new AppointmentException("Doctor and date are required");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + doctorId
                        )
                );

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        String dayName = dayOfWeek.toString();

        List<DoctorSchedule> schedules =
                doctorScheduleRepository
                        .findByDoctorIdAndDayOfWeek(
                                doctorId,
                                dayName
                        );

        if (schedules.isEmpty()) {
            throw new AppointmentException(
                    "Doctor is not available on " + dayName
            );
        }

        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentDate(
                                doctorId,
                                date
                        );

        List<LocalTime> bookedSlots =
                appointments.stream()
                        .map(Appointment::getAppointmentTime)
                        .toList();

        List<LocalTime> availableSlots = new ArrayList<>();

        for (DoctorSchedule schedule : schedules) {

            LocalTime currentTime =
                    schedule.getStartTime();

            while (currentTime.isBefore(
                    schedule.getEndTime())) {

                if (!bookedSlots.contains(currentTime)) {
                    availableSlots.add(currentTime);
                }

                currentTime = currentTime.plusMinutes(15);
            }
        }

        return new AvailableSlotResponseDTO(
                doctor.getId(),
                doctor.getName(),
                date,
                availableSlots
        );
    }
}