package com.hospital.hospital_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> { })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Public authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Allow Swagger UI and OpenAPI documentation
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Admin workspace
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Role-specific dashboards
                        .requestMatchers("/api/dashboard/doctor").hasRole("DOCTOR")
                        .requestMatchers("/api/dashboard/patient").hasRole("PATIENT")
                        .requestMatchers("/api/dashboard").hasRole("ADMIN")

                        // Patients
                        .requestMatchers(HttpMethod.GET, "/api/patients/profile").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/patients", "/api/patients/*")
                            .hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/patients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/*").hasRole("ADMIN")

                        // Doctors: patients and doctors may browse; only admin mutates
                        .requestMatchers(HttpMethod.GET, "/api/doctors/me").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/doctors", "/api/doctors/*")
                            .hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/doctors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/doctors/*").hasRole("ADMIN")

                        // Departments: all authenticated users may read; admin writes
                        .requestMatchers(HttpMethod.GET, "/api/departments", "/api/departments/*")
                            .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/departments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/departments/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/departments/*").hasRole("ADMIN")

                        // Schedules: anyone authenticated may read; doctor/admin may write
                        .requestMatchers(HttpMethod.GET, "/api/schedules", "/api/schedules/*", "/api/schedules/doctor/*")
                            .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/schedules/doctor/*").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/schedules/*").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/schedules/*").hasAnyRole("ADMIN", "DOCTOR")

                        // Appointments
                        .requestMatchers(HttpMethod.GET, "/api/appointments/available-slots").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/appointments/my").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/doctor/my").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/appointments").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/*").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/*/status").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/*").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/*").hasRole("PATIENT")

                        // Prescriptions
                        .requestMatchers(HttpMethod.GET, "/api/prescriptions/appointment/*").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/prescriptions/patient/*").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/prescriptions/doctor/*").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/prescriptions/*").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/prescriptions").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/prescriptions").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/prescriptions/*").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/prescriptions/*").hasRole("ADMIN")

                        // Medical records
                        .requestMatchers(HttpMethod.GET, "/api/medical-records/patient/*").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/medical-records/doctor/*").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/medical-records/appointment/*").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/medical-records/*").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/api/medical-records").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/medical-records").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/medical-records/*").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/medical-records/*").hasAnyRole("ADMIN", "DOCTOR")

                        // Notifications are always scoped to the logged-in user by the service
                        .requestMatchers("/api/notifications/**").authenticated()

                        // Resources and allocations are admin operations
                        .requestMatchers("/api/resources/**", "/api/resource-allocations/**").hasRole("ADMIN")

                        // Audit logs are admin operations except /my for any authenticated user
                        .requestMatchers("/api/audit-logs/my").authenticated()
                        .requestMatchers("/api/audit-logs/**").hasRole("ADMIN")

                        // Everything else must be last. Never put requestMatchers after anyRequest.
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaimAsString("role");
            if (role == null || role.isBlank()) {
                return Collections.emptyList();
            }
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        });
        return converter;
    }
}
