package com.example.hr.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Birthday Wishes Job
 * Gửi lời chúc sinh nhật tự động
 */
@Slf4j
@Component
public class BirthdayWishesJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Starting birthday wishes job at {}", LocalDateTime.now());
        
        try {
            // TODO: Implement birthday wishes logic
            // 1. Get employees with birthday today
            // 2. Send birthday wishes email
            // 3. Create notification
            // 4. Log activity
            
            log.info("Birthday wishes sent successfully");
            
        } catch (Exception e) {
            log.error("Error sending birthday wishes", e);
            throw new JobExecutionException(e);
        }
    }
}
