package com.example.hr.kafka.consumer;

import com.example.hr.kafka.events.RecruitmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer cho Recruitment Events
 * Xử lý các sự kiện tuyển dụng
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitmentEventConsumer {

    @KafkaListener(topics = "${kafka.topics.recruitment}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeRecruitmentEvent(RecruitmentEvent event) {
        try {
            log.info("Received recruitment event: applicationId={}, eventType={}, candidate={}", 
                    event.getApplicationId(), event.getEventType(), event.getCandidateName());
            
            switch (event.getEventType()) {
                case "APPLICATION_RECEIVED":
                    handleApplicationReceived(event);
                    break;
                case "SCREENING":
                    handleScreening(event);
                    break;
                case "INTERVIEW_SCHEDULED":
                    handleInterviewScheduled(event);
                    break;
                case "OFFER_SENT":
                    handleOfferSent(event);
                    break;
                case "HIRED":
                    handleHired(event);
                    break;
                case "REJECTED":
                    handleRejected(event);
                    break;
                default:
                    log.warn("Unknown recruitment event type: {}", event.getEventType());
            }
            
        } catch (Exception e) {
            log.error("Error processing recruitment event: applicationId={}", 
                    event.getApplicationId(), e);
        }
    }

    private void handleApplicationReceived(RecruitmentEvent event) {
        log.info("Processing application received: candidate={}, job={}", 
                event.getCandidateName(), event.getJobTitle());
        // TODO: Send confirmation email to candidate, notify HR
    }

    private void handleScreening(RecruitmentEvent event) {
        log.info("Processing screening: candidate={}", event.getCandidateName());
        // TODO: Update application status, assign to recruiter
    }

    private void handleInterviewScheduled(RecruitmentEvent event) {
        log.info("Processing interview scheduled: candidate={}, interviewer={}, date={}", 
                event.getCandidateName(), event.getInterviewerName(), event.getInterviewDate());
        // TODO: Send calendar invite to candidate and interviewer
    }

    private void handleOfferSent(RecruitmentEvent event) {
        log.info("Processing offer sent: candidate={}, job={}", 
                event.getCandidateName(), event.getJobTitle());
        // TODO: Send offer letter email, track response
    }

    private void handleHired(RecruitmentEvent event) {
        log.info("Processing hired: candidate={}, job={}", 
                event.getCandidateName(), event.getJobTitle());
        // TODO: Trigger onboarding process, create employee account
    }

    private void handleRejected(RecruitmentEvent event) {
        log.info("Processing rejection: candidate={}", event.getCandidateName());
        // TODO: Send rejection email, update application status
    }
}
