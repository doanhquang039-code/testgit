package com.example.hr.scheduler;

import com.example.hr.service.BenefitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler tự động expire phúc lợi hết hạn.
 * Chạy hàng ngày lúc 1:00 AM.
 */
@Component
public class BenefitExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(BenefitExpiryScheduler.class);
    private final BenefitService benefitService;

    public BenefitExpiryScheduler(BenefitService benefitService) {
        this.benefitService = benefitService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void autoExpireBenefits() {
        log.info("Running benefit expiry check...");
        try {
            int expired = benefitService.autoExpireBenefits();
            log.info("Auto-expired {} benefits", expired);
        } catch (Exception e) {
            log.error("Benefit expiry check failed: {}", e.getMessage());
        }
    }
}
