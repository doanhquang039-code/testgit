package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hr.enums.LeaveStatus;
import com.example.hr.enums.LeaveType;
import com.example.hr.enums.UserStatus;
import com.example.hr.models.LeaveRequest;
import com.example.hr.models.User;
import com.example.hr.repository.LeaveRequestRepository;
import com.example.hr.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
public class LeaveRequestController {

    @Autowired
    private LeaveRequestRepository leaveRepository;
    @Autowired
    private UserRepository userRepository;

    // ==================== ADMIN ====================

    @GetMapping("/admin/leaves")
    public String listAll(Model model) {
        model.addAttribute("leaves", leaveRepository.findAllWithUser(null));
        return "admin/leave-list";
    }

    @GetMapping("/admin/leaves/add")
    public String showAddForm(Model model) {
        model.addAttribute("leaveRequest", new LeaveRequest());
        model.addAttribute("users", userRepository.findByStatus(UserStatus.ACTIVE));
        model.addAttribute("statuses", LeaveStatus.values());
        return "admin/leave-form";
    }

    @GetMapping("/admin/leaves/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        LeaveRequest lr = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID không tồn tại: " + id));
        model.addAttribute("leaveRequest", lr);
        model.addAttribute("users", userRepository.findByStatus(UserStatus.ACTIVE));
        model.addAttribute("statuses", LeaveStatus.values());
        return "admin/leave-form";
    }

    @PostMapping("/admin/leaves/save")
    public String save(@ModelAttribute("leaveRequest") LeaveRequest lr) {
        if (lr.getId() == null && lr.getStatus() == null) {
            lr.setStatus(LeaveStatus.PENDING);
        }
        leaveRepository.save(lr);
        return "redirect:/admin/leaves";
    }

    @GetMapping("/admin/leaves/approve/{id}")
    public String approve(@PathVariable Integer id) {
        LeaveRequest lr = leaveRepository.findById(id).orElseThrow();
        lr.setStatus(LeaveStatus.APPROVED);
        User approver = userRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Người duyệt không tồn tại"));
        lr.setApprovedBy(approver);
        leaveRepository.save(lr);
        return "redirect:/admin/leaves";
    }

    @GetMapping("/admin/leaves/reject/{id}")
    public String reject(@PathVariable Integer id) {
        LeaveRequest lr = leaveRepository.findById(id).orElseThrow();
        lr.setStatus(LeaveStatus.REJECTED);
        leaveRepository.save(lr);
        return "redirect:/admin/leaves";
    }

    @GetMapping("/admin/leaves/delete/{id}")
    public String delete(@PathVariable Integer id) {
        leaveRepository.deleteById(id);
        return "redirect:/admin/leaves";
    }

    // ==================== USER SELF-SERVICE ====================

    @GetMapping("/user/leaves")
    public String userLeaves(Authentication auth, Model model) {
        User currentUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        List<LeaveRequest> myLeaves = leaveRepository.findByUser(currentUser);
        model.addAttribute("myLeaves", myLeaves);
        model.addAttribute("currentUser", currentUser);
        return "user1/leave-request";
    }

    @PostMapping("/user/leaves/submit")
    public String submitLeave(@RequestParam String leaveType,
                              @RequestParam String startDate,
                              @RequestParam String endDate,
                              @RequestParam String reason,
                              Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        LeaveRequest lr = new LeaveRequest();
        lr.setUser(currentUser);
        lr.setLeaveType(LeaveType.valueOf(leaveType));
        lr.setStartDate(LocalDate.parse(startDate));
        lr.setEndDate(LocalDate.parse(endDate));
        lr.setReason(reason);
        lr.setStatus(LeaveStatus.PENDING);

        leaveRepository.save(lr);
        return "redirect:/user/leaves";
    }
}