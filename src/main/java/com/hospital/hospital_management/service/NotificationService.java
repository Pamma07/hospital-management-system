package com.hospital.hospital_management.service;

import com.hospital.hospital_management.dto.NotificationRequestDTO;
import com.hospital.hospital_management.dto.NotificationResponseDTO;
import com.hospital.hospital_management.entity.Notification;
import com.hospital.hospital_management.exception.AppointmentException;
import com.hospital.hospital_management.exception.ResourceNotFoundException;
import com.hospital.hospital_management.repository.NotificationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    // CREATE NOTIFICATION
    public NotificationResponseDTO createNotification(
            NotificationRequestDTO request) {

        validateRequest(request);

        String username = getLoggedInUsername();

        Notification notification = new Notification();

        notification.setUsername(username);
        notification.setMessage(request.getMessage());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification =
                notificationRepository.save(notification);

        return convertToResponse(savedNotification);
    }

    // GET MY NOTIFICATIONS
    public List<NotificationResponseDTO> getMyNotifications() {

        String username = getLoggedInUsername();

        return notificationRepository
                .findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET NOTIFICATION BY ID
    public NotificationResponseDTO getNotificationById(
            Long id) {

        String username = getLoggedInUsername();

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        // User can only access their own notification
        if (!notification.getUsername().equals(username)) {
            throw new ResourceNotFoundException(
                    "Notification not found with id: " + id
            );
        }

        return convertToResponse(notification);
    }

    // GET UNREAD NOTIFICATIONS
    public List<NotificationResponseDTO> getUnreadNotifications() {

        String username = getLoggedInUsername();

        return notificationRepository
                .findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .filter(notification -> !notification.isRead())
                .map(this::convertToResponse)
                .toList();
    }

    // COUNT UNREAD NOTIFICATIONS
    public long countUnreadNotifications() {

        String username = getLoggedInUsername();

        return notificationRepository
                .findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .filter(notification -> !notification.isRead())
                .count();
    }

    // MARK NOTIFICATION AS READ
    public NotificationResponseDTO markAsRead(
            Long id) {

        String username = getLoggedInUsername();

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        // Prevent one user from modifying another user's notification
        if (!notification.getUsername().equals(username)) {
            throw new ResourceNotFoundException(
                    "Notification not found with id: " + id
            );
        }

        notification.setRead(true);

        Notification updatedNotification =
                notificationRepository.save(notification);

        return convertToResponse(updatedNotification);
    }

    // DELETE NOTIFICATION
    public void deleteNotification(Long id) {

        String username = getLoggedInUsername();

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        // Prevent one user from deleting another user's notification
        if (!notification.getUsername().equals(username)) {
            throw new ResourceNotFoundException(
                    "Notification not found with id: " + id
            );
        }

        notificationRepository.delete(notification);
    }

    // VALIDATION
    private void validateRequest(
            NotificationRequestDTO request) {

        if (request.getMessage() == null ||
                request.getMessage().isBlank()) {

            throw new AppointmentException(
                    "Notification message is required"
            );
        }
    }

    // GET LOGGED-IN USERNAME FROM JWT
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

    // ENTITY → RESPONSE DTO
    private NotificationResponseDTO convertToResponse(
            Notification notification) {

        return new NotificationResponseDTO(
                notification.getId(),
                notification.getUsername(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}