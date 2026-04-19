package com.example.hr.service;

import com.example.hr.dto.OvertimeRequestDTO;
import com.example.hr.enums.OvertimeStatus;
import com.example.hr.exception.ApprovalWorkflowException;
import com.example.hr.exception.BusinessValidationException;
import com.example.hr.exception.ResourceNotFoundException;
import com.example.hr.models.OvertimeRequest;
import com.example.hr.models.User;
import com.example.hr.repository.OvertimeRequestRepository;
import com.example.hr.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service xử lý đơn đăng ký làm thêm giờ (OT).
 * Bao gồm: submit, approve/reject, tính tiền OT, validate giờ tối đa.
 */
@Service
@Transactional
public class OvertimeService {

    private static final BigDecimal MAX_MONTHLY_OT_HOURS = new BigDecimal("40"); // 40 giờ/tháng
    private static final BigDecimal MAX_DAILY_OT_HOURS = new BigDecimal("4");    // 4 giờ/ngày
    private static final BigDecimal WEEKDAY_MULTIPLIER = new BigDecimal("1.5");
    private static final BigDecimal WEEKEND_MULTIPLIER = new BigDecimal("2.0");
    private static final BigDecimal HOLIDAY_MULTIPLIER = new BigDecimal("3.0");

    private final OvertimeRequestRepository overtimeRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OvertimeService(OvertimeRequestRepository overtimeRepository,
                            UserRepository userRepository,
                            ApplicationEventPublisher eventPublisher) {
        this.overtimeRepository = overtimeRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Lấy tất cả đơn OT.
     */
    @Transactional(readOnly = true)
    public List<OvertimeRequest> getAllRequests() {
        return overtimeRepository.findAll();
    }

    /**
     * Lấy đơn OT theo ID.
     */
    @Transactional(readOnly = true)
    public OvertimeRequest getRequestById(Integer id) {
        return overtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn OT", id));
    }

    /**
     * Lấy đơn OT theo user.
     */
    @Transactional(readOnly = true)
    public List<OvertimeRequest> getRequestsByUser(Integer userId) {
        return overtimeRepository.findByUserId(userId);
    }

    /**
     * Lấy đơn OT đang chờ duyệt.
     */
    @Transactional(readOnly = true)
    public List<OvertimeRequest> getPendingRequests() {
        return overtimeRepository.findPendingRequests();
    }

    /**
     * Submit đơn OT mới.
     */
    public OvertimeRequest submitRequest(OvertimeRequestDTO dto) {
        // Validate user
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên", dto.getUserId()));

        // Validate không trùng ngày
        long existingCount = overtimeRepository.countByUserAndDate(dto.getUserId(), dto.getOvertimeDate());
        if (existingCount > 0) {
            throw new BusinessValidationException("Bạn đã có đơn OT cho ngày này");
        }

        // Create request
        OvertimeRequest request = new OvertimeRequest();
        request.setUser(user);
        request.setOvertimeDate(dto.getOvertimeDate());
        request.setStartTime(dto.getStartTime());
        request.setEndTime(dto.getEndTime());
        request.setReason(dto.getReason());
        request.setStatus(OvertimeStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        // Auto-calculate hours
        request.calculateTotalHours();

        // Validate giờ OT
        validateOvertimeHours(request);

        // Auto-determine multiplier
        request.determineMultiplier(isHoliday(dto.getOvertimeDate()));

        return overtimeRepository.save(request);
    }

    /**
     * Duyệt đơn OT.
     */
    public OvertimeRequest approveRequest(Integer requestId, User approver) {
        OvertimeRequest request = getRequestById(requestId);

        if (request.getStatus() != OvertimeStatus.PENDING) {
            throw new ApprovalWorkflowException("Chỉ có thể duyệt đơn đang ở trạng thái Chờ duyệt");
        }

        // Kiểm tra không tự duyệt
        if (request.getUser().getId().equals(approver.getId())) {
            throw new ApprovalWorkflowException("Không thể tự duyệt đơn OT của mình");
        }

        request.approve(approver);
        OvertimeRequest saved = overtimeRepository.save(request);

        return saved;
    }

    /**
     * Từ chối đơn OT.
     */
    public OvertimeRequest rejectRequest(Integer requestId, User approver, String reason) {
        OvertimeRequest request = getRequestById(requestId);

        if (request.getStatus() != OvertimeStatus.PENDING) {
            throw new ApprovalWorkflowException("Chỉ có thể từ chối đơn đang ở trạng thái Chờ duyệt");
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessValidationException("Phải cung cấp lý do từ chối");
        }

        request.reject(approver, reason);
        return overtimeRepository.save(request);
    }

    /**
     * Hủy đơn OT (bởi nhân viên).
     */
    public OvertimeRequest cancelRequest(Integer requestId, Integer userId) {
        OvertimeRequest request = getRequestById(requestId);

        if (!request.getUser().getId().equals(userId)) {
            throw new ApprovalWorkflowException("Bạn chỉ có thể hủy đơn OT của mình");
        }

        if (request.getStatus() != OvertimeStatus.PENDING) {
            throw new ApprovalWorkflowException("Chỉ có thể hủy đơn đang Chờ duyệt");
        }

        request.setStatus(OvertimeStatus.CANCELLED);
        request.setUpdatedAt(LocalDateTime.now());
        return overtimeRepository.save(request);
    }

    /**
     * Tính tiền OT cho một nhân viên trong tháng.
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateMonthlyOvertimePay(Integer userId, int month, int year, BigDecimal hourlyRate) {
        List<OvertimeRequest> approvedRequests = overtimeRepository
                .findByUserAndMonth(userId, month, year).stream()
                .filter(r -> r.getStatus() == OvertimeStatus.APPROVED)
                .toList();

        BigDecimal totalPay = BigDecimal.ZERO;
        for (OvertimeRequest request : approvedRequests) {
            totalPay = totalPay.add(request.calculateOvertimePay(hourlyRate));
        }
        return totalPay;
    }

    /**
     * Tổng giờ OT đã duyệt trong tháng cho nhân viên.
     */
    @Transactional(readOnly = true)
    public BigDecimal getApprovedHoursInMonth(Integer userId, int month, int year) {
        return overtimeRepository.sumApprovedOvertimeHours(userId, month, year);
    }

    /**
     * Thống kê OT theo phòng ban.
     */
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getOvertimeByDepartment(int year) {
        return overtimeRepository.sumOvertimeByDepartment(year).stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (BigDecimal) row[1]
                ));
    }

    /**
     * Đếm đơn OT theo trạng thái.
     */
    @Transactional(readOnly = true)
    public long countByStatus(OvertimeStatus status) {
        return overtimeRepository.countByStatus(status);
    }

    /**
     * Lấy danh sách OT đã duyệt trong khoảng thời gian.
     */
    @Transactional(readOnly = true)
    public List<OvertimeRequest> getApprovedBetweenDates(LocalDate start, LocalDate end) {
        return overtimeRepository.findApprovedBetweenDates(start, end);
    }

    // --- Validation helpers ---

    private void validateOvertimeHours(OvertimeRequest request) {
        // Validate max daily hours
        if (!request.isValidDuration()) {
            throw new BusinessValidationException("Giờ OT phải từ 0 đến " + MAX_DAILY_OT_HOURS + " giờ/ngày");
        }

        // Validate max monthly hours
        int month = request.getOvertimeDate().getMonthValue();
        int year = request.getOvertimeDate().getYear();
        BigDecimal currentMonthlyHours = overtimeRepository
                .sumApprovedOvertimeHours(request.getUser().getId(), month, year);

        BigDecimal projectedTotal = currentMonthlyHours.add(request.getTotalHours());
        if (projectedTotal.compareTo(MAX_MONTHLY_OT_HOURS) > 0) {
            throw new BusinessValidationException(
                    String.format("Vượt quá giới hạn OT tháng. Hiện tại: %.1f giờ, Yêu cầu: %.1f giờ, Tối đa: %s giờ",
                            currentMonthlyHours.doubleValue(),
                            request.getTotalHours().doubleValue(),
                            MAX_MONTHLY_OT_HOURS));
        }
    }

    /**
     * Kiểm tra ngày có phải ngày lễ không (VN holidays).
     * Simplified — chỉ check các ngày lễ cố định.
     */
    private boolean isHoliday(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        // Tết Dương lịch
        if (month == 1 && day == 1) return true;
        // Giỗ Tổ Hùng Vương (10/3 âm lịch — simplified)
        // Giải phóng miền Nam 30/4
        if (month == 4 && day == 30) return true;
        // Quốc tế Lao động 1/5
        if (month == 5 && day == 1) return true;
        // Quốc khánh 2/9
        if (month == 9 && day == 2) return true;

        return false;
    }
}
