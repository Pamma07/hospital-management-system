package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.AuthResponseDTO;
import com.hospital.hospital_management.dto.LoginRequestDTO;
import com.hospital.hospital_management.dto.RegisterRequestDTO;
import com.hospital.hospital_management.entity.Patient;
import com.hospital.hospital_management.entity.Role;
import com.hospital.hospital_management.entity.User;
import com.hospital.hospital_management.exception.AppointmentException;
import com.hospital.hospital_management.repository.PatientRepository;
import com.hospital.hospital_management.repository.UserRepository;
import com.hospital.hospital_management.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PatientRepository patientRepository;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PatientRepository patientRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.patientRepository = patientRepository;
    }

    // =========================
    // REGISTER USER
    // =========================

    @Transactional
    public User register(RegisterRequestDTO request) {

        if (request.getUsername() == null ||
                request.getUsername().isBlank()) {

            throw new AppointmentException(
                    "Username is required"
            );
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {

            throw new AppointmentException(
                    "Password is required"
            );
        }

        if (request.getRole() == null) {

            throw new AppointmentException(
                    "Role is required"
            );
        }

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new AppointmentException(
                    "Username already exists"
            );
        }

        // Create User
        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());

        user.setEnabled(true);

        // Save User
        User savedUser = userRepository.save(user);

        // =========================
        // CREATE PATIENT
        // =========================

        if (request.getRole() == Role.PATIENT) {

            if (request.getName() == null ||
                    request.getName().isBlank()) {

                throw new AppointmentException(
                        "Patient name is required"
                );
            }

            if (request.getAge() == null) {

                throw new AppointmentException(
                        "Patient age is required"
                );
            }

            if (request.getAge() <= 0) {

                throw new AppointmentException(
                        "Patient age must be greater than 0"
                );
            }

            Patient patient = new Patient();

            patient.setUsername(
                    request.getUsername()
            );

            patient.setName(
                    request.getName()
            );

            patient.setAge(
                    request.getAge()
            );

            patient.setGender(
                    request.getGender()
            );

            patient.setPhone(
                    request.getPhone()
            );

            patient.setEmail(
                    request.getEmail()
            );

            patient.setAddress(
                    request.getAddress()
            );

            patient.setUser(savedUser);

            patientRepository.save(patient);
        }

        return savedUser;
    }

    // =========================
    // LOGIN USER
    // =========================

    public AuthResponseDTO login(
            LoginRequestDTO request) {

        if (request.getUsername() == null ||
                request.getUsername().isBlank()) {

            throw new AppointmentException(
                    "Username is required"
            );
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {

            throw new AppointmentException(
                    "Password is required"
            );
        }

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        User user =
                userRepository.findByUsername(
                        request.getUsername()
                ).orElseThrow(() ->
                        new AppointmentException(
                                "User not found"
                        )
                );

        String token =
                jwtService.generateToken(user);

        return new AuthResponseDTO(token);
    }
}