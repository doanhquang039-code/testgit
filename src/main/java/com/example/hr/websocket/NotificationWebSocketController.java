package com.example.hr.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * WebSocket Controller for Real-time Notifications
 * Xử lý real-time notifications qua WebSocket
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class NotificationWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Broadcast notification to all users
     */
    @MessageMapping("/notification/broadcast")
    @SendTo("/topic/notifications")
    public Map<String, Object> broadcastNotification(Map<String, Object> notification) {
        log.info("Broadcasting notification: {}", notification);
        notification.put("timestamp", LocalDateTime.now());
        return notification;
    }

    /**
     * Send notification to specific user
     */
    public void sendToUser(String username, Map<String, Object> notification) {
        log.info("Sending notification to user {}: {}", username, notification);
        notification.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
    }

    /**
     * Send attendance update
     */
    public void sendAttendanceUpdate(Map<String, Object> update) {
        log.info("Sending attendance update: {}", update);
        messagingTemplate.convertAndSend("/topic/attendance", update);
    }

    /**
     * Send leave request update
     */
    public void sendLeaveRequestUpdate(Map<String, Object> update) {
        log.info("Sending leave request update: {}", update);
        messagingTemplate.convertAndSend("/topic/leave-requests", update);
    }
}
