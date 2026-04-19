package com.example.hr.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility tính lương theo quy định VN.
 * Bao gồm: thuế TNCN luỹ tiến, bảo hiểm, OT multiplier.
 */
public final class PayrollCalculator {

    private PayrollCalculator() {} // utility class

    // Giảm trừ gia cảnh
    private static final BigDecimal PERSONAL_DEDUCTION = new BigDecimal("11000000");
    private static final BigDecimal DEPENDENT_DEDUCTION = new BigDecimal("4400000");

    /**
     * Tính thuế Thu nhập cá nhân (PIT) theo biểu thuế luỹ tiến từng phần VN.
     *
     * Bậc 1: Đến 5 triệu: 5%
     * Bậc 2: 5-10 triệu: 10%
     * Bậc 3: 10-18 triệu: 15%
     * Bậc 4: 18-32 triệu: 20%
     * Bậc 5: 32-52 triệu: 25%
     * Bậc 6: 52-80 triệu: 30%
     * Bậc 7: Trên 80 triệu: 35%
     */
    public static BigDecimal calculatePIT(BigDecimal taxableIncome, int dependents) {
        if (taxableIncome == null) return BigDecimal.ZERO;

        // Apply deductions
        BigDecimal deduction = PERSONAL_DEDUCTION
                .add(DEPENDENT_DEDUCTION.multiply(BigDecimal.valueOf(dependents)));
        BigDecimal assessableIncome = taxableIncome.subtract(deduction);

        if (assessableIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal remaining = assessableIncome;

        // Bracket definitions: (upper limit, rate)
        BigDecimal[][] brackets = {
                {new BigDecimal("5000000"), new BigDecimal("0.05")},
                {new BigDecimal("5000000"), new BigDecimal("0.10")},   // 5-10M
                {new BigDecimal("8000000"), new BigDecimal("0.15")},   // 10-18M
                {new BigDecimal("14000000"), new BigDecimal("0.20")},  // 18-32M
                {new BigDecimal("20000000"), new BigDecimal("0.25")},  // 32-52M
                {new BigDecimal("28000000"), new BigDecimal("0.30")},  // 52-80M
        };

        for (BigDecimal[] bracket : brackets) {
            BigDecimal bracketSize = bracket[0];
            BigDecimal rate = bracket[1];

            if (remaining.compareTo(bracketSize) <= 0) {
                tax = tax.add(remaining.multiply(rate));
                remaining = BigDecimal.ZERO;
                break;
            } else {
                tax = tax.add(bracketSize.multiply(rate));
                remaining = remaining.subtract(bracketSize);
            }
        }

        // Bậc 7: phần còn lại trên 80M
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            tax = tax.add(remaining.multiply(new BigDecimal("0.35")));
        }

        return tax.setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Tính BHXH (8%), BHYT (1.5%), BHTN (1%) — phần người lao động.
     */
    public static BigDecimal calculateInsuranceDeduction(BigDecimal baseSalary) {
        if (baseSalary == null) return BigDecimal.ZERO;
        BigDecimal rate = new BigDecimal("0.105"); // 8% + 1.5% + 1%
        return baseSalary.multiply(rate).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Tính BHXH phần doanh nghiệp đóng.
     * BHXH: 17.5%, BHYT: 3%, BHTN: 1%
     */
    public static BigDecimal calculateEmployerInsurance(BigDecimal baseSalary) {
        if (baseSalary == null) return BigDecimal.ZERO;
        BigDecimal rate = new BigDecimal("0.215"); // 17.5% + 3% + 1%
        return baseSalary.multiply(rate).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Tính lương giờ.
     */
    public static BigDecimal calculateHourlyRate(BigDecimal monthlySalary, int standardHours) {
        if (monthlySalary == null || standardHours == 0) return BigDecimal.ZERO;
        return monthlySalary.divide(BigDecimal.valueOf(standardHours), 0, RoundingMode.HALF_UP);
    }

    /**
     * Tính tiền OT.
     */
    public static BigDecimal calculateOTPay(BigDecimal hourlyRate, BigDecimal hours, BigDecimal multiplier) {
        if (hourlyRate == null || hours == null || multiplier == null) return BigDecimal.ZERO;
        return hourlyRate.multiply(hours).multiply(multiplier).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Format tiền VND.
     */
    public static String formatVND(BigDecimal amount) {
        if (amount == null) return "0 ₫";
        return String.format("%,.0f ₫", amount);
    }

    /**
     * Format tiền VND ngắn gọn (triệu).
     */
    public static String formatVNDShort(BigDecimal amount) {
        if (amount == null) return "0";
        double millions = amount.doubleValue() / 1_000_000;
        if (millions >= 1) {
            return String.format("%.1ftr", millions);
        }
        double thousands = amount.doubleValue() / 1_000;
        return String.format("%.0fk", thousands);
    }
}
