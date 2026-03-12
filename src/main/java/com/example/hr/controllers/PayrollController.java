package com.example.hr.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hr.models.Payroll;
import com.example.hr.models.User;
import com.example.hr.models.Contract;
import com.example.hr.enums.PaymentStatus;
import com.example.hr.repository.PayrollRepository;
import com.example.hr.repository.UserRepository;
import com.example.hr.repository.ContractRepository;

// ... (các import giữ nguyên)

@Controller
@RequestMapping("/admin/payroll")
public class PayrollController {

    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;

  @GetMapping
public String list(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
    List<Payroll> payrolls = payrollRepository.findAllWithUser(keyword);
    model.addAttribute("payrolls", payrolls);
    model.addAttribute("keyword", keyword);
    return "admin/payroll-list";
}

    @PostMapping("/generate/{userId}")
    public String generatePayroll(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<Contract> contracts = contractRepository.findByUser(user);
        if (contracts.isEmpty()) {
            throw new RuntimeException("Nhân viên này chưa có hợp đồng nào!");
        }
        Contract contract = contracts.get(0);

        // 1. Lấy lương cơ bản (BigDecimal) từ đúng trường trong Model
        BigDecimal baseSalary = contract.getBaseSalaryOnContract();

        // 2. Tính hệ số phụ cấp đảm bảo đồng nhất kiểu dữ liệu
        // Nếu getAllowanceCoeff() trả về kiểu double hoặc float
        // BỎ BigDecimal.valueOf(...) đi vì nó đã là BigDecimal rồi
        BigDecimal allowanceCoeff = (user.getPosition() != null)
                ? user.getPosition().getAllowanceCoeff()
                : BigDecimal.ZERO;

        // Phép nhân giữ nguyên vì cả hai đều là BigDecimal
        BigDecimal bonusAmount = baseSalary.multiply(allowanceCoeff);

        // Lưu ý: Dùng .multiply() để nhân hai đối tượng BigDecimal

        Payroll payroll = new Payroll();
        payroll.setUser(user);
        payroll.setMonth(LocalDate.now().getMonthValue());
        payroll.setYear(LocalDate.now().getYear());
        payroll.setBaseSalary(baseSalary);
        payroll.setBonus(bonusAmount);
        payroll.setDeductions(BigDecimal.ZERO);
        payroll.setPaymentStatus(com.example.hr.enums.PaymentStatus.PENDING);

        payrollRepository.save(payroll);
        return "redirect:/admin/payroll";
    }

    @GetMapping("/mark-paid/{id}")
    public String markAsPaid(@PathVariable Integer id) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow();
        payroll.setPaymentStatus(PaymentStatus.PAID);
        payrollRepository.save(payroll);
        return "redirect:/admin/payroll";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        payrollRepository.deleteById(id);
        return "redirect:/admin/payroll";
    }
}
