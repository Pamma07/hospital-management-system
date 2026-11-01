package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.DoctorScheduleRequestDTO;
import com.hospital.hospital_management.dto.DoctorScheduleResponseDTO;
import com.hospital.hospital_management.entity.Doctor;
import com.hospital.hospital_management.entity.DoctorSchedule;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.DoctorRepository;
import com.hospital.hospital_management.repository.DoctorScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public DoctorScheduleService(
            DoctorScheduleRepository scheduleRepository,
            DoctorRepository doctorRepository) {

        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void validateDoctorAccess(Doctor doctor) {
        if (isAdmin()) {
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || doctor == null || doctor.getUsername() == null ||
                !doctor.getUsername().equals(authentication.getName())) {
            throw new com.hospital.hospital_management.exception.AppointmentException(
                    "You can only manage your own schedule");
        }
    }

    // CREATE
    public DoctorScheduleResponseDTO createSchedule(
            Long doctorId,
            DoctorScheduleRequestDTO request) {

        validateScheduleRequest(request);

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + doctorId
                        )
                );

        validateDoctorAccess(doctor);

        DoctorSchedule schedule = new DoctorSchedule();

        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(
                request.getDayOfWeek().toUpperCase()
        );
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        DoctorSchedule savedSchedule =
                scheduleRepository.save(schedule);

        return convertToResponseDTO(savedSchedule);
    }

    // GET ALL
    public List<DoctorScheduleResponseDTO> getAllSchedules() {

        return scheduleRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET BY ID
    public DoctorScheduleResponseDTO getScheduleById(Long id) {

        return scheduleRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule not found with id: " + id
                        )
                );
    }

    // GET BY DOCTOR
    public List<DoctorScheduleResponseDTO> getSchedulesByDoctor(
            Long doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with id: " + doctorId
            );
        }

        return scheduleRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // UPDATE
    public DoctorScheduleResponseDTO updateSchedule(
            Long id,
            DoctorScheduleRequestDTO request) {

        DoctorSchedule existingSchedule =
                scheduleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Schedule not found with id: " + id
                                )
                        );

        validateDoctorAccess(existingSchedule.getDoctor());
        validateScheduleRequest(request);

        existingSchedule.setDayOfWeek(
                request.getDayOfWeek().toUpperCase()
        );

        existingSchedule.setStartTime(
                request.getStartTime()
        );

        existingSchedule.setEndTime(
                request.getEndTime()
        );

        DoctorSchedule updatedSchedule =
                scheduleRepository.save(existingSchedule);

        return convertToResponseDTO(updatedSchedule);
    }

    // DELETE
    public void deleteSchedule(Long id) {

        DoctorSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Schedule not found with id: " + id
                        )
                );

        validateDoctorAccess(schedule.getDoctor());
        scheduleRepository.delete(schedule);
    }

    private void validateScheduleRequest(DoctorScheduleRequestDTO request) {
        if (request == null || request.getDayOfWeek() == null || request.getDayOfWeek().isBlank()
                || request.getStartTime() == null || request.getEndTime() == null) {
            throw new com.hospital.hospital_management.exception.AppointmentException(
                    "Day, start time and end time are required");
        }
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new com.hospital.hospital_management.exception.AppointmentException(
                    "End time must be after start time");
        }
    }

    // ENTITY → RESPONSE DTO
    private DoctorScheduleResponseDTO convertToResponseDTO(
            DoctorSchedule schedule) {

        Long doctorId = null;
        String doctorName = null;

        if (schedule.getDoctor() != null) {
            doctorId = schedule.getDoctor().getId();
            doctorName = schedule.getDoctor().getName();
        }

        return new DoctorScheduleResponseDTO(
                schedule.getId(),
                doctorId,
                doctorName,
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}