package com.example.hr.service;

import com.example.hr.dto.PayrollSummaryDTO;
import com.example.hr.exception.PayrollCalculationException;
import com.example.hr.exception.ResourceNotFoundException;
import com.example.hr.models.Payroll;
import com.example.hr.models.User;
import com.example.hr.repository.PayrollRepository;
import com.example.hr.repository.UserRepository;
import com.example.hr.util.PayrollCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Service tính lương nâng cao.
 * Bao gồm: OT, bonus, deductions, thuế TNCN, BHXH/BHYT/BHTN.
 */
@Service
@Transactional
public class AdvancedPayrollService {

    private static final Logger log = LoggerFactory.getLogger(AdvancedPayrollService.class);

    // Tỷ lệ bảo hiểm (người lao động đóng)
    private static final BigDecimal SOCIAL_INSURANCE_RATE = new BigDecimal("0.08");     // BHXH 8%
    private static final BigDecimal HEALTH_INSURANCE_RATE = new BigDecimal("0.015");    // BHYT 1.5%
    private static final BigDecimal UNEMPLOYMENT_INSURANCE_RATE = new BigDecimal("0.01"); // BHTN 1%

    // Giảm trừ gia cảnh (simplified)
    private static final BigDecimal PERSONAL_DEDUCTION = new BigDecimal("11000000");     // 11 triệu/tháng
    private static final BigDecimal DEPENDENT_DEDUCTION = new BigDecimal("4400000");      // 4.4 triệu/người phụ thuộc

    // Số giờ làm việc tiêu chuẩn mỗi tháng
    private static final BigDecimal STANDARD_HOURS_PER_MONTH = new BigDecimal("176"); // 22 ngày x 8 giờ

    private final PayrollRepository payrollRepository;
    private final UserRepository userRepository;
    private final NewOvertimeService overtimeService;
    private final BenefitService benefitService;

    public AdvancedPayrollService(PayrollRepository payrollRepository,
                                    UserRepository userRepository,
                                    NewOvertimeService overtimeService,
                                    BenefitService benefitService) {
        this.payrollRepository = payrollRepository;
        this.userRepository = userRepository;
        this.overtimeService = overtimeService;
        this.benefitService = benefitService;
    }

    /**
     * Tính bảng lương chi tiết cho một nhân viên.
     */
    @Transactional(readOnly = true)
    public PayrollSummaryDTO calculateDetailedPayroll(Integer userId, int month, int year) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên", userId));

        // Tìm payroll record
        Payroll payroll = payrollRepository.findAll().stream()
                .filter(p -> p.getUser().getId().equals(userId)
                        && p.getMonth() == month && p.getYear() == year)
                .findFirst()
                .orElse(null);

        BigDecimal baseSalary = payroll != null ? payroll.getBaseSalary() : BigDecimal.ZERO;
        BigDecimal bonus = payroll != null ? payroll.getBonus() : BigDecimal.ZERO;

        try {
            return buildPayrollSummary(user, baseSalary, bonus, month, year);
        } catch (Exception e) {
            throw new PayrollCalculationException("Lỗi tính lương cho " + user.getFullName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Tính bảng lương cho toàn bộ nhân viên trong tháng.
     */
    @Transactional(readOnly = true)
    public List<PayrollSummaryDTO> calculateAllPayrolls(int month, int year) {
        List<PayrollSummaryDTO> summaries = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            try {
                PayrollSummaryDTO summary = calculateDetailedPayroll(user.getId(), month, year);
                summaries.add(summary);
            } catch (Exception e) {
                log.error("Failed to calculate payroll for user {}: {}", user.getUsername(), e.getMessage());
            }
        }

        return summaries;
    }

    /**
     * Tính tổng chi phí lương trong tháng.
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPayrollCost(int month, int year) {
        return calculateAllPayrolls(month, year).stream()
                .map(PayrollSummaryDTO::getGrossSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Tính lương giờ từ lương tháng.
     */
    public BigDecimal calculateHourlyRate(BigDecimal monthlySalary) {
        if (monthlySalary == null || monthlySalary.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return monthlySalary.divide(STANDARD_HOURS_PER_MONTH, 0, RoundingMode.HALF_UP);
    }

    /**
     * Tính thuế TNCN theo biểu thuế luỹ tiến VN.
     */
    public BigDecimal calculatePersonalIncomeTax(BigDecimal taxableIncome, int dependents) {
        return PayrollCalculator.calculatePIT(taxableIncome, dependents);
    }

    // --- Private helpers ---

    private PayrollSummaryDTO buildPayrollSummary(User user, BigDecimal baseSalary,
                                                    BigDecimal bonus, int month, int year) {
        PayrollSummaryDTO summary = new PayrollSummaryDTO();
        summary.setUserId(user.getId());
        summary.setEmployeeName(user.getFullName());
        summary.setMonth(month);
        summary.setYear(year);

        if (user.getDepartment() != null) {
            summary.setDepartmentName(user.getDepartment().getDepartmentName());
        }
        if (user.getPosition() != null) {
            summary.setPositionName(user.getPosition().getPositionName());
        }

        // 1. Base salary
        summary.setBaseSalary(baseSalary);

        // 2. Allowance (from position)
        BigDecimal allowance = BigDecimal.ZERO;
        if (user.getPosition() != null && user.getPosition().getAllowanceCoeff() != null) {
            allowance = baseSalary.multiply(user.getPosition().getAllowanceCoeff())
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        }
        summary.setAllowance(allowance);

        // 3. Overtime pay - TODO: Update with new overtime model
        BigDecimal hourlyRate = calculateHourlyRate(baseSalary);
        BigDecimal overtimeHours = BigDecimal.ZERO; // overtimeService.getApprovedHoursInMonth(user.getId(), month, year);
        BigDecimal overtimePay = BigDecimal.ZERO; // overtimeService.calculateMonthlyOvertimePay(user.getId(), month, year, hourlyRate);
        summary.setOvertimeHours(overtimeHours);
        summary.setOvertimePay(overtimePay);

        // 4. Bonus
        summary.setBonus(bonus);

        // 5. Insurance deductions (calculated on base salary)
        BigDecimal socialInsurance = baseSalary.multiply(SOCIAL_INSURANCE_RATE)
                .setScale(0, RoundingMode.HALF_UP);
        BigDecimal healthInsurance = baseSalary.multiply(HEALTH_INSURANCE_RATE)
                .setScale(0, RoundingMode.HALF_UP);
        BigDecimal unemploymentInsurance = baseSalary.multiply(UNEMPLOYMENT_INSURANCE_RATE)
                .setScale(0, RoundingMode.HALF_UP);

        summary.setSocialInsurance(socialInsurance);
        summary.setHealthInsurance(healthInsurance);
        summary.setUnemploymentInsurance(unemploymentInsurance);

        // 6. Personal income tax
        BigDecimal totalInsurance = socialInsurance.add(healthInsurance).add(unemploymentInsurance);
        BigDecimal grossIncome = baseSalary.add(allowance).add(overtimePay).add(bonus);
        BigDecimal taxableIncome = grossIncome.subtract(totalInsurance).subtract(PERSONAL_DEDUCTION);
        BigDecimal pit = PayrollCalculator.calculatePIT(taxableIncome, 0);
        summary.setPersonalIncomeTax(pit);

        // 7. Calculate totals
        summary.calculateNetSalary();

        return summary;
    }
}
