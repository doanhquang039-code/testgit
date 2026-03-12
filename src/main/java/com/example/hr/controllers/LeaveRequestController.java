package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hr.models.LeaveRequest;
import com.example.hr.models.User;
import com.example.hr.enums.LeaveStatus;
import com.example.hr.enums.UserStatus;
import com.example.hr.repository.LeaveRequestRepository;
import com.example.hr.repository.UserRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/leaves")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestRepository leaveRepository;
    @Autowired
    private UserRepository userRepository;

   @GetMapping
public String listAll(Model model) {
    model.addAttribute("leaves", leaveRepository.findAllWithUser(null));
    return "admin/leave-list";
}

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("leaveRequest", new LeaveRequest());
        model.addAttribute("users", userRepository.findByStatus(UserStatus.ACTIVE));
        model.addAttribute("statuses", LeaveStatus.values());
        return "admin/leave-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        LeaveRequest lr = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID không tồn tại: " + id));
        model.addAttribute("leaveRequest", lr);
        model.addAttribute("users", userRepository.findByStatus(UserStatus.ACTIVE));
        model.addAttribute("statuses", LeaveStatus.values());
        return "admin/leave-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("leaveRequest") LeaveRequest lr) {
        if (lr.getId() == null && lr.getStatus() == null) {
            lr.setStatus(LeaveStatus.PENDING);
        }
        leaveRepository.save(lr);
        return "redirect:/admin/leaves";
    }

    // PHẦN SỬA LỖI DÒNG 68
    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Integer id) {
        LeaveRequest lr = leaveRepository.findById(id).orElseThrow();
        lr.setStatus(LeaveStatus.APPROVED);

        // Tìm đối tượng User quản lý (ID = 2)
        User approver = userRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Người duyệt không tồn tại"));

        // Gán đối tượng User thay vì String "2"
        lr.setApprovedBy(approver);

        leaveRepository.save(lr);
        return "redirect:/admin/leaves";
    }

    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Integer id) {
        LeaveRequest lr = leaveRepository.findById(id).orElseThrow();
        lr.setStatus(LeaveStatus.REJECTED);
        leaveRepository.save(lr);
        return "redirect:/admin/leaves";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        leaveRepository.deleteById(id);
        return "redirect:/admin/leaves";
    }
}