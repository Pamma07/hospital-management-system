package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.AuthResponseDTO;
import com.hospital.hospital_management.dto.LoginRequestDTO;
import com.hospital.hospital_management.dto.RegisterRequestDTO;
import com.hospital.hospital_management.entity.User;
import com.hospital.hospital_management.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody RegisterRequestDTO request) {

        User user = authService.register(request);

        Map<String, Object> response = new HashMap<>();

        response.put("message", "User registered successfully");
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        AuthResponseDTO response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }
}