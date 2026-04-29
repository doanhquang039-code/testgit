package com.example.hr.kafka.consumer;

import com.example.hr.kafka.events.AttendanceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer cho Attendance Events
 * Xử lý các sự kiện chấm công
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceEventConsumer {

    @KafkaListener(topics = "${kafka.topics.attendance}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAttendanceEvent(AttendanceEvent event) {
        try {
            log.info("Received attendance event: userId={}, eventType={}, timestamp={}", 
                    event.getUserId(), event.getEventType(), event.getTimestamp());
            
            // Xử lý logic business
            switch (event.getEventType()) {
                case "CHECK_IN":
                    handleCheckIn(event);
                    break;
                case "CHECK_OUT":
                    handleCheckOut(event);
                    break;
                case "LATE":
                    handleLateArrival(event);
                    break;
                case "EARLY_LEAVE":
                    handleEarlyLeave(event);
                    break;
                case "ABSENT":
                    handleAbsent(event);
                    break;
                default:
                    log.warn("Unknown attendance event type: {}", event.getEventType());
            }
            
        } catch (Exception e) {
            log.error("Error processing attendance event: userId={}", event.getUserId(), e);
        }
    }

    private void handleCheckIn(AttendanceEvent event) {
        log.info("Processing check-in: user={}, location={}", 
                event.getFullName(), event.getLocation());
        // TODO: Cập nhật database, gửi notification, tính toán overtime, etc.
    }

    private void handleCheckOut(AttendanceEvent event) {
        log.info("Processing check-out: user={}, location={}", 
                event.getFullName(), event.getLocation());
        // TODO: Tính toán working hours, update attendance record
    }

    private void handleLateArrival(AttendanceEvent event) {
        log.info("Processing late arrival: user={}", event.getFullName());
        // TODO: Gửi warning notification, cập nhật late count
    }

    private void handleEarlyLeave(AttendanceEvent event) {
        log.info("Processing early leave: user={}", event.getFullName());
        // TODO: Gửi notification cho manager
    }

    private void handleAbsent(AttendanceEvent event) {
        log.info("Processing absent: user={}", event.getFullName());
        // TODO: Gửi notification, cập nhật attendance record
    }
}
