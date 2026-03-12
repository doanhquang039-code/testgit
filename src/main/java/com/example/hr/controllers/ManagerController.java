package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.hr.repository.*;
import com.example.hr.enums.LeaveStatus;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private LeaveRequestRepository leaveRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Thống kê nhanh cho Manager
        model.addAttribute("totalEmployees", userRepository.count());
        model.addAttribute("pendingLeaves", leaveRepository.findByStatus(LeaveStatus.PENDING).size());

        // Lấy danh sách việc cần làm và đơn nghỉ mới nhất
        model.addAttribute("recentLeaves", leaveRepository.findAll());
        model.addAttribute("recentTasks", taskRepository.findAll());

        return "manager/dashboard";
    }
}