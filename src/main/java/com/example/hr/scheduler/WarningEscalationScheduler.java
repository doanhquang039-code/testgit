package com.example.hr.scheduler;

import com.example.hr.service.WarningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler kiểm tra cảnh cáo chưa acknowledge.
 * Chạy hàng ngày lúc 9:00 AM.
 */
@Component
public class WarningEscalationScheduler {

    private static final Logger log = LoggerFactory.getLogger(WarningEscalationScheduler.class);
    private final WarningService warningService;

    public WarningEscalationScheduler(WarningService warningService) {
        this.warningService = warningService;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void checkUnacknowledgedWarnings() {
        log.info("Checking unacknowledged warnings...");
        try {
            var unacknowledged = warningService.processUnacknowledgedWarnings();
            if (!unacknowledged.isEmpty()) {
                log.warn("Found {} unacknowledged warnings needing attention", unacknowledged.size());
            }
        } catch (Exception e) {
            log.error("Warning escalation check failed: {}", e.getMessage());
        }
    }
}
