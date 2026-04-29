package com.example.hr.kafka.consumer;

import com.example.hr.kafka.events.NotificationEvent;
import com.example.hr.kafka.events.TrainingEvent;
import com.example.hr.kafka.producer.HREventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Consumer cho Training Events
 * Xử lý các sự kiện đào tạo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingEventConsumer {

    private final HREventProducer eventProducer;

    @KafkaListener(topics = "${kafka.topics.training}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTrainingEvent(TrainingEvent event) {
        try {
            log.info("Received training event: employeeId={}, eventType={}, training={}", 
                    event.getEmployeeId(), event.getEventType(), event.getTrainingName());
            
            switch (event.getEventType()) {
                case "ENROLLED":
                    handleEnrolled(event);
                    break;
                case "STARTED":
                    handleStarted(event);
                    break;
                case "COMPLETED":
                    handleCompleted(event);
                    break;
                case "CANCELLED":
                    handleCancelled(event);
                    break;
                case "CERTIFICATE_ISSUED":
                    handleCertificateIssued(event);
                    break;
                default:
                    log.warn("Unknown training event type: {}", event.getEventType());
            }
            
        } catch (Exception e) {
            log.error("Error processing training event: employeeId={}", event.getEmployeeId(), e);
        }
    }

    private void handleEnrolled(TrainingEvent event) {
        log.info("Processing training enrollment: employee={}, training={}", 
                event.getEmployeeName(), event.getTrainingName());
        
        // Gửi notification cho employee
        NotificationEvent notification = new NotificationEvent(
                "EMAIL",
                Collections.singletonList(event.getEmployeeId()),
                "Đăng ký khóa đào tạo thành công",
                String.format("Bạn đã đăng ký thành công khóa đào tạo '%s'. Ngày bắt đầu: %s",
                        event.getTrainingName(), event.getStartDate()),
                "MEDIUM",
                "TRAINING",
                event.getTrainingId(),
                "TRAINING",
                LocalDateTime.now()
        );
        eventProducer.publishNotificationEvent(notification);
    }

    private void handleStarted(TrainingEvent event) {
        log.info("Processing training start: employee={}, training={}", 
                event.getEmployeeName(), event.getTrainingName());
        // TODO: Track training progress
    }

    private void handleCompleted(TrainingEvent event) {
        log.info("Processing training completion: employee={}, training={}, completion={}%", 
                event.getEmployeeName(), event.getTrainingName(), event.getCompletionPercentage());
        
        // Gửi notification cho employee
        NotificationEvent notification = new NotificationEvent(
                "EMAIL",
                Collections.singletonList(event.getEmployeeId()),
                "Hoàn thành khóa đào tạo",
                String.format("Chúc mừng! Bạn đã hoàn thành khóa đào tạo '%s' với tỷ lệ %.0f%%",
                        event.getTrainingName(), event.getCompletionPercentage()),
                "HIGH",
                "TRAINING",
                event.getTrainingId(),
                "TRAINING",
                LocalDateTime.now()
        );
        eventProducer.publishNotificationEvent(notification);
    }

    private void handleCancelled(TrainingEvent event) {
        log.info("Processing training cancellation: employee={}, training={}", 
                event.getEmployeeName(), event.getTrainingName());
        // TODO: Update training status, notify manager
    }

    private void handleCertificateIssued(TrainingEvent event) {
        log.info("Processing certificate issuance: employee={}, training={}", 
                event.getEmployeeName(), event.getTrainingName());
        
        // Gửi notification cho employee
        NotificationEvent notification = new NotificationEvent(
                "EMAIL",
                Collections.singletonList(event.getEmployeeId()),
                "Chứng chỉ đào tạo đã được cấp",
                String.format("Chứng chỉ hoàn thành khóa đào tạo '%s' đã được cấp. Vui lòng kiểm tra hồ sơ của bạn.",
                        event.getTrainingName()),
                "HIGH",
                "TRAINING",
                event.getTrainingId(),
                "TRAINING",
                LocalDateTime.now()
        );
        eventProducer.publishNotificationEvent(notification);
    }
}
