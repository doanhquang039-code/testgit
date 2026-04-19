package com.example.hr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Cấu hình async processing và scheduling.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncSchedulerConfig {
}
