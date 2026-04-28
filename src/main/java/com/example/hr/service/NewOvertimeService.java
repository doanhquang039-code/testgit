package com.example.hr.service;

import com.example.hr.models.OvertimeRequest;
import com.example.hr.models.User;
import com.example.hr.repository.OvertimeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NewOvertimeService {
    
    private final OvertimeRequestRepository overtimeRepository;
    
    /**
     * Create overtime request
     */
    public OvertimeRequest createRequest(User user, LocalDate overtimeDate, Double hours, String reason) {
        OvertimeRequest request = new OvertimeRequest();
        request.setUser(user);
        request.setOvertimeDate(overtimeDate);
        request.setHours(hours);
        request.setReason(reason);
        request.setStatus("PENDING");
        request.setMultiplier(1.5); // Default multiplier
        
        return overtimeRepository.save(request);
    }
    
    /**
     * Get overtime requests by user
     */
    @Transactional(readOnly = true)
    public List<OvertimeRequest> getUserRequests(User user) {
        return overtimeRepository.findByUser(user);
    }
    
    /**
     * Get pending requests
     */
    @Transactional(readOnly = true)
    public List<OvertimeRequest> getPendingRequests() {
        return overtimeRepository.findByStatus("PENDING");
    }
    
    /**
     * Approve overtime request
     */
    public OvertimeRequest approveRequest(Integer requestId, User approver) {
        Optional<OvertimeRequest> requestOpt = overtimeRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Overtime request not found");
        }
        
        OvertimeRequest request = requestOpt.get();
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Request is not pending");
        }
        
        request.setStatus("APPROVED");
        request.setApprovedBy(approver);
        request.setApprovedAt(java.time.LocalDateTime.now());
        
        return overtimeRepository.save(request);
    }
    
    /**
     * Reject overtime request
     */
    public OvertimeRequest rejectRequest(Integer requestId, User approver, String reason) {
        Optional<OvertimeRequest> requestOpt = overtimeRepository.findById(requestId);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Overtime request not found");
        }
        
        OvertimeRequest request = requestOpt.get();
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Request is not pending");
        }
        
        request.setStatus("REJECTED");
        request.setApprovedBy(approver);
        request.setApprovedAt(java.time.LocalDateTime.now());
        request.setRejectionReason(reason);
        
        return overtimeRepository.save(request);
    }
    
    /**
     * Get total approved overtime hours for user in date range
     */
    @Transactional(readOnly = true)
    public Double getTotalApprovedHours(User user, LocalDate startDate, LocalDate endDate) {
        Double total = overtimeRepository.getTotalApprovedHours(user, startDate, endDate);
        return total != null ? total : 0.0;
    }
    
    /**
     * Get overtime requests in date range
     */
    @Transactional(readOnly = true)
    public List<OvertimeRequest> getRequestsInDateRange(LocalDate startDate, LocalDate endDate) {
        return overtimeRepository.findByOvertimeDateBetween(startDate, endDate);
    }
}
