package com.example.hr.kafka.consumer;

import com.example.hr.kafka.events.NotificationEvent;
import com.example.hr.kafka.events.PerformanceReviewEvent;
import com.example.hr.kafka.producer.HREventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Consumer cho Performance Review Events
 * Xử lý các sự kiện đánh giá hiệu suất
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceReviewEventConsumer {

    private final HREventProducer eventProducer;

    @KafkaListener(topics = "${kafka.topics.performance-reviews}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePerformanceReviewEvent(PerformanceReviewEvent event) {
        try {
            log.info("Received performance review event: employeeId={}, eventType={}, rating={}", 
                    event.getEmployeeId(), event.getEventType(), event.getOverallRating());
            
            switch (event.getEventType()) {
                case "CREATED":
                    handleReviewCreated(event);
                    break;
                case "SUBMITTED":
                    handleReviewSubmitted(event);
                    break;
                case "APPROVED":
                    handleReviewApproved(event);
                    break;
                case "COMPLETED":
                    handleReviewCompleted(event);
                    break;
                default:
                    log.warn("Unknown performance review event type: {}", event.getEventType());
            }
            
        } catch (Exception e) {
            log.error("Error processing performance review event: employeeId={}", 
                    event.getEmployeeId(), e);
        }
    }

    private void handleReviewCreated(PerformanceReviewEvent event) {
        log.info("Processing review creation: employee={}, reviewer={}", 
                event.getEmployeeName(), event.getReviewerName());
        
        // Gửi notification cho employee
        NotificationEvent notification = new NotificationEvent(
                "EMAIL",
                Collections.singletonList(event.getEmployeeId()),
                "Đánh giá hiệu suất mới",
                String.format("Bạn có đánh giá hiệu suất mới từ %s cho kỳ %s",
                        event.getReviewerName(), event.getReviewPeriod()),
                "MEDIUM",
                "PERFORMANCE",
                event.getReviewId(),
                "PERFORMANCE_REVIEW",
                LocalDateTime.now()
        );
        eventProducer.publishNotificationEvent(notification);
    }

    private void handleReviewSubmitted(PerformanceReviewEvent event) {
        log.info("Processing review submission: employee={}", event.getEmployeeName());
        // TODO: Notify HR for approval
    }

    private void handleReviewApproved(PerformanceReviewEvent event) {
        log.info("Processing review approval: employee={}", event.getEmployeeName());
        // TODO: Make review visible to employee
    }

    private void handleReviewCompleted(PerformanceReviewEvent event) {
        log.info("Processing review completion: employee={}, rating={}", 
                event.getEmployeeName(), event.getOverallRating());
        
        // Gửi notification cho employee
        NotificationEvent notification = new NotificationEvent(
                "EMAIL",
                Collections.singletonList(event.getEmployeeId()),
                "Đánh giá hiệu suất hoàn tất",
                String.format("Đánh giá hiệu suất kỳ %s của bạn đã hoàn tất. Điểm tổng: %.2f",
                        event.getReviewPeriod(), event.getOverallRating()),
                "HIGH",
                "PERFORMANCE",
                event.getReviewId(),
                "PERFORMANCE_REVIEW",
                LocalDateTime.now()
        );
        eventProducer.publishNotificationEvent(notification);
    }
}
