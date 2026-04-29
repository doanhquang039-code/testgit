package com.example.hr.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event cho Recruitment (Tuyển dụng)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentEvent {
    private Integer applicationId;
    private Integer jobPostingId;
    private String jobTitle;
    private String candidateName;
    private String candidateEmail;
    private String eventType; // APPLICATION_RECEIVED, SCREENING, INTERVIEW_SCHEDULED, OFFER_SENT, HIRED, REJECTED
    private String status;
    private Integer interviewerId;
    private String interviewerName;
    private LocalDateTime interviewDate;
    private LocalDateTime timestamp;
}
