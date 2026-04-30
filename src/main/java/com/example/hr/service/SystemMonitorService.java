package com.example.hr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemMonitorService {

    private final DataSource dataSource;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;

    /**
     * Lấy system health metrics
     */
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        
        health.put("server", getServerMetrics());
        health.put("database", getDatabaseMetrics());
        health.put("cache", getCacheMetrics());
        health.put("memory", getMemoryMetrics());
        health.put("timestamp", System.currentTimeMillis());
        
        return health;
    }

    /**
     * Server metrics
     */
    private Map<String, Object> getServerMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            
            metrics.put("cpuLoad", osBean.getSystemLoadAverage());
            metrics.put("availableProcessors", osBean.getAvailableProcessors());
            metrics.put("osName", osBean.getName());
            metrics.put("osVersion", osBean.getVersion());
            metrics.put("osArch", osBean.getArch());
            metrics.put("status", "UP");
        } catch (Exception e) {
            log.error("Error getting server metrics", e);
            metrics.put("status", "ERROR");
            metrics.put("error", e.getMessage());
        }
        
        return metrics;
    }

    /**
     * Database metrics
     */
    private Map<String, Object> getDatabaseMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            metrics.put("status", "UP");
            metrics.put("database", conn.getMetaData().getDatabaseProductName());
            metrics.put("version", conn.getMetaData().getDatabaseProductVersion());
            metrics.put("url", conn.getMetaData().getURL());
            metrics.put("connectionValid", conn.isValid(5));
        } catch (Exception e) {
            log.error("Error getting database metrics", e);
            metrics.put("status", "DOWN");
            metrics.put("error", e.getMessage());
        }
        
        return metrics;
    }

    /**
     * Cache metrics
     */
    private Map<String, Object> getCacheMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            // Redis metrics
            RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
            metrics.put("redis", Map.of(
                "status", connection.ping() != null ? "UP" : "DOWN",
                "dbSize", connection.dbSize()
            ));
            connection.close();
            
            // Cache names
            metrics.put("cacheNames", cacheManager.getCacheNames());
            metrics.put("status", "UP");
        } catch (Exception e) {
            log.error("Error getting cache metrics", e);
            metrics.put("status", "DOWN");
            metrics.put("error", e.getMessage());
        }
        
        return metrics;
    }

    /**
     * Memory metrics
     */
    private Map<String, Object> getMemoryMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            Runtime runtime = Runtime.getRuntime();
            
            long maxMemory = runtime.maxMemory();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            metrics.put("maxMemory", formatBytes(maxMemory));
            metrics.put("totalMemory", formatBytes(totalMemory));
            metrics.put("usedMemory", formatBytes(usedMemory));
            metrics.put("freeMemory", formatBytes(freeMemory));
            metrics.put("usagePercent", (usedMemory * 100.0 / maxMemory));
            
            metrics.put("heap", Map.of(
                "used", formatBytes(memoryBean.getHeapMemoryUsage().getUsed()),
                "max", formatBytes(memoryBean.getHeapMemoryUsage().getMax()),
                "committed", formatBytes(memoryBean.getHeapMemoryUsage().getCommitted())
            ));
            
            metrics.put("status", "UP");
        } catch (Exception e) {
            log.error("Error getting memory metrics", e);
            metrics.put("status", "ERROR");
            metrics.put("error", e.getMessage());
        }
        
        return metrics;
    }

    /**
     * Format bytes to human readable
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * Get quick stats for dashboard
     */
    public Map<String, Object> getQuickStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            
            stats.put("memoryUsagePercent", (usedMemory * 100.0 / maxMemory));
            stats.put("databaseStatus", isDatabaseHealthy() ? "UP" : "DOWN");
            stats.put("cacheStatus", isCacheHealthy() ? "UP" : "DOWN");
            stats.put("overallStatus", "UP");
        } catch (Exception e) {
            log.error("Error getting quick stats", e);
            stats.put("overallStatus", "ERROR");
        }
        
        return stats;
    }

    private boolean isDatabaseHealthy() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(5);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCacheHealthy() {
        try {
            RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
            boolean healthy = connection.ping() != null;
            connection.close();
            return healthy;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get performance metrics for admin dashboard
     */
    public Map<String, Object> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            Runtime runtime = Runtime.getRuntime();
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            
            // CPU metrics
            metrics.put("cpuLoad", osBean.getSystemLoadAverage());
            metrics.put("availableProcessors", osBean.getAvailableProcessors());
            
            // Memory metrics
            long maxMemory = runtime.maxMemory();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            metrics.put("memoryUsagePercent", (usedMemory * 100.0 / maxMemory));
            metrics.put("heapUsed", memoryBean.getHeapMemoryUsage().getUsed());
            metrics.put("heapMax", memoryBean.getHeapMemoryUsage().getMax());
            
            // System status
            metrics.put("databaseHealthy", isDatabaseHealthy());
            metrics.put("cacheHealthy", isCacheHealthy());
            
            // Uptime
            metrics.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
            
        } catch (Exception e) {
            log.error("Error getting performance metrics", e);
            metrics.put("error", e.getMessage());
        }
        
        return metrics;
    }
}
