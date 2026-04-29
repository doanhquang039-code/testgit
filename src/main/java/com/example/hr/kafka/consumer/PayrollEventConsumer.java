package com.example.hr.kafka.consumer;

import com.example.hr.kafka.events.NotificationEvent;
import com.example.hr.kafka.events.PayrollEvent;
import com.example.hr.kafka.producer.HREventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Consumer cho Payroll Events
 * Xử lý các sự kiện bảng lương
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollEventConsumer {

    private final HREventProducer eventProducer;

    @KafkaListener(topics = "${kafka.topics.payroll}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePayrollEvent(PayrollEvent event) {
        try {
            log.info("Received payroll event: userId={}, eventType={}, month={}/{}", 
                    event.getUserId(), event.getEventType(), event.getMonth(), event.getYear());
            
            switch (event.getEventType()) {
                case "GENERATED":
                    handlePayrollGenerated(event);
                    break;
                case "APPROVED":
                    handlePayrollApproved(event);
                    break;
                case "PAID":
                    handlePayrollPaid(event);
                    break;
                case "REJECTED":
                    handlePayrollRejected(event);
                    break;
                default:
                    log.warn("Unknown payroll event type: {}", event.getEventType());
            }
            
        } catch (Exception e) {
            log.error("Error processing payroll event: userId={}", event.getUserId(), e);
        }
    }

    private void handlePayrollGenerated(PayrollEvent event) {
        log.info("Processing payroll generation: user={}, netSalary={}", 
                event.getFullName(), event.getNetSalary());
        // TODO: Generate payroll report, send to HR for review
    }

    private void handlePayrollApproved(PayrollEvent event) {
        log.info("Processing payroll approval: user={}, netSalary={}", 
                event.getFullName(), event.getNetSalary());
        // TODO: Prepare for payment processing
    }

    private void handlePayrollPaid(PayrollEvent event) {
        log.info("Processing payroll payment: user={}, netSalary={}", 
                event.getFullName(), event.getNetSalary());
        
        // Gửi notification cho employee
        NotificationEvent notification = new NotificationEvent(
                "EMAIL",
                Collections.singletonList(event.getUserId()),
                "Lương tháng " + event.getMonth() + "/" + event.getYear() + " đã được thanh toán",
                String.format("Lương tháng %d/%d của bạn đã được thanh toán. Số tiền: %s VND",
                        event.getMonth(), event.getYear(), event.getNetSalary()),
                "HIGH",
                "PAYROLL",
                event.getPayrollId(),
                "PAYROLL",
                LocalDateTime.now()
        );
        eventProducer.publishNotificationEvent(notification);
    }

    private void handlePayrollRejected(PayrollEvent event) {
        log.info("Processing payroll rejection: user={}", event.getFullName());
        // TODO: Notify HR, require recalculation
    }
}
