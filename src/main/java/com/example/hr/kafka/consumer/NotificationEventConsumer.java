package com.example.hr.kafka.consumer;

import com.example.hr.kafka.events.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer cho Notification Events
 * Xử lý gửi thông báo qua các kênh khác nhau
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    // TODO: Inject EmailService, SMSService, PushNotificationService

    @KafkaListener(topics = "${kafka.topics.notifications}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeNotificationEvent(NotificationEvent event) {
        try {
            log.info("Received notification event: type={}, category={}, recipients={}", 
                    event.getNotificationType(), event.getCategory(), event.getRecipientUserIds().size());
            
            switch (event.getNotificationType()) {
                case "EMAIL":
                    sendEmailNotification(event);
                    break;
                case "SMS":
                    sendSMSNotification(event);
                    break;
                case "PUSH":
                    sendPushNotification(event);
                    break;
                case "IN_APP":
                    sendInAppNotification(event);
                    break;
                default:
                    log.warn("Unknown notification type: {}", event.getNotificationType());
            }
            
        } catch (Exception e) {
            log.error("Error processing notification event: category={}", event.getCategory(), e);
        }
    }

    private void sendEmailNotification(NotificationEvent event) {
        log.info("Sending email notification: subject={}, recipients={}", 
                event.getSubject(), event.getRecipientUserIds().size());
        // TODO: Implement email sending logic
        // emailService.sendEmail(event.getRecipientUserIds(), event.getSubject(), event.getMessage());
    }

    private void sendSMSNotification(NotificationEvent event) {
        log.info("Sending SMS notification: recipients={}", event.getRecipientUserIds().size());
        // TODO: Implement SMS sending logic
        // smsService.sendSMS(event.getRecipientUserIds(), event.getMessage());
    }

    private void sendPushNotification(NotificationEvent event) {
        log.info("Sending push notification: recipients={}", event.getRecipientUserIds().size());
        // TODO: Implement push notification logic
        // pushService.sendPush(event.getRecipientUserIds(), event.getSubject(), event.getMessage());
    }

    private void sendInAppNotification(NotificationEvent event) {
        log.info("Sending in-app notification: recipients={}", event.getRecipientUserIds().size());
        // TODO: Save notification to database for in-app display
        // notificationRepository.save(notification);
    }
}
