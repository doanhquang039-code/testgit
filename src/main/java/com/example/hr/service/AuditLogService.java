package com.example.hr.service;

import com.example.hr.models.AuditLog;
import com.example.hr.models.User;
import com.example.hr.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Log một action của user
     */
    @Async
    public void logAction(User user, String action, String entityType, Integer entityId,
                         String oldValue, String newValue, HttpServletRequest request) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUser(user);
            auditLog.setAction(action);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setOldValue(oldValue);
            auditLog.setNewValue(newValue);
            auditLog.setIpAddress(getClientIP(request));
            auditLog.setUserAgent(request.getHeader("User-Agent"));
            auditLog.setRequestMethod(request.getMethod());
            auditLog.setRequestUrl(request.getRequestURI());
            auditLog.setStatus("SUCCESS");
            auditLog.setTimestamp(LocalDateTime.now());

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error logging audit action", e);
        }
    }

    /**
     * Log failed action
     */
    @Async
    public void logFailedAction(User user, String action, String errorMessage, HttpServletRequest request) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUser(user);
            auditLog.setAction(action);
            auditLog.setStatus("FAILED");
            auditLog.setErrorMessage(errorMessage);
            auditLog.setIpAddress(getClientIP(request));
            auditLog.setUserAgent(request.getHeader("User-Agent"));
            auditLog.setRequestMethod(request.getMethod());
            auditLog.setRequestUrl(request.getRequestURI());
            auditLog.setTimestamp(LocalDateTime.now());

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error logging failed action", e);
        }
    }

    /**
     * Lấy tất cả audit logs với phân trang
     */
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    /**
     * Lấy audit logs của một user
     */
    public Page<AuditLog> getAuditLogsByUser(User user, Pageable pageable) {
        return auditLogRepository.findByUserOrderByTimestampDesc(user, pageable);
    }

    /**
     * Tìm kiếm audit logs theo action
     */
    public Page<AuditLog> searchByAction(String action, Pageable pageable) {
        return auditLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc(action, pageable);
    }

    /**
     * Lấy audit logs theo entity type
     */
    public Page<AuditLog> getAuditLogsByEntityType(String entityType, Pageable pageable) {
        return auditLogRepository.findByEntityTypeOrderByTimestampDesc(entityType, pageable);
    }

    /**
     * Lấy audit logs theo khoảng thời gian
     */
    public Page<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByDateRange(startDate, endDate, pageable);
    }

    /**
     * Lấy thống kê audit logs
     */
    public Map<String, Object> getAuditStatistics() {
        long totalActions = auditLogRepository.count();
        long failedActions = auditLogRepository.countFailedActions();
        long todayActions = auditLogRepository.countActionsSince(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        
        List<Object[]> topActions = auditLogRepository.getTopActions();
        
        return Map.of(
            "totalActions", totalActions,
            "failedActions", failedActions,
            "todayActions", todayActions,
            "successRate", totalActions > 0 ? ((totalActions - failedActions) * 100.0 / totalActions) : 0,
            "topActions", topActions
        );
    }

    /**
     * Xóa audit logs cũ (retention policy)
     */
    @Transactional
    public void cleanupOldLogs(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        auditLogRepository.deleteByTimestampBefore(cutoffDate);
        log.info("Cleaned up audit logs older than {} days", retentionDays);
    }

    /**
     * Lấy IP address của client
     */
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    /**
     * Get recent logs for admin dashboard
     */
    public List<AuditLog> getRecentLogs(int limit) {
        return auditLogRepository.findTop10ByOrderByTimestampDesc()
                .stream()
                .limit(limit)
                .toList();
    }

    /**
     * Get activity statistics for admin dashboard
     */
    public Map<String, Object> getActivityStatistics() {
        return getAuditStatistics();
    }

    /**
     * Get audit log by ID
     */
    public AuditLog getLogById(Integer id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found with id: " + id));
    }

    /**
     * Search logs with multiple criteria
     */
    public Page<AuditLog> searchLogs(String action, String entityType, Integer userId, 
                                   LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (action != null && !action.trim().isEmpty()) {
            return searchByAction(action, pageable);
        }
        if (entityType != null && !entityType.trim().isEmpty()) {
            return getAuditLogsByEntityType(entityType, pageable);
        }
        if (startDate != null && endDate != null) {
            return getAuditLogsByDateRange(startDate, endDate, pageable);
        }
        return getAllAuditLogs(pageable);
    }

    /**
     * Get activity timeline for a user
     */
    public List<AuditLog> getActivityTimeline(Integer userId, int limit) {
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .limit(limit)
                .toList();
    }
}
