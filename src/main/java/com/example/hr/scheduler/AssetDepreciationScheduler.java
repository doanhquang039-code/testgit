package com.example.hr.scheduler;

import com.example.hr.service.AssetManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler cập nhật khấu hao tài sản.
 * Chạy đầu mỗi tháng lúc 2:00 AM.
 */
@Component
public class AssetDepreciationScheduler {

    private static final Logger log = LoggerFactory.getLogger(AssetDepreciationScheduler.class);
    private final AssetManagementService assetService;

    public AssetDepreciationScheduler(AssetManagementService assetService) {
        this.assetService = assetService;
    }

    @Scheduled(cron = "0 0 2 1 * ?")
    public void updateDepreciation() {
        log.info("Running monthly asset depreciation update...");
        try {
            int count = assetService.updateAllDepreciations();
            log.info("Updated depreciation for {} assets", count);
        } catch (Exception e) {
            log.error("Asset depreciation update failed: {}", e.getMessage());
        }
    }
}
