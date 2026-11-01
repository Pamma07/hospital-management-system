package com.hospital.hospital_management.controller;

import com.hospital.hospital_management.dto.NotificationRequestDTO;
import com.hospital.hospital_management.dto.NotificationResponseDTO;
import com.hospital.hospital_management.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    // CREATE NOTIFICATION
    @PostMapping
    public ResponseEntity<NotificationResponseDTO>
    createNotification(
            @RequestBody NotificationRequestDTO request) {

        NotificationResponseDTO response =
                notificationService.createNotification(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET MY NOTIFICATIONS
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>>
    getMyNotifications() {

        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );
    }

    // GET NOTIFICATION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO>
    getNotificationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                notificationService.getNotificationById(id)
        );
    }

    // GET MY UNREAD NOTIFICATIONS
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDTO>>
    getUnreadNotifications() {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications()
        );
    }

    // COUNT MY UNREAD NOTIFICATIONS
    @GetMapping("/unread/count")
    public ResponseEntity<Long>
    countUnreadNotifications() {

        return ResponseEntity.ok(
                notificationService.countUnreadNotifications()
        );
    }

    // MARK NOTIFICATION AS READ
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDTO>
    markAsRead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }

    // DELETE NOTIFICATION
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteNotification(
            @PathVariable Long id) {

        notificationService.deleteNotification(id);

        return ResponseEntity.ok(
                "Notification deleted successfully"
        );
    }
}