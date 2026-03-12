package com.example.hr.controllers;

import com.example.hr.models.User;
import com.example.hr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user1") // Đường dẫn riêng dành cho Nhân viên
public class User1Controller {

    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private LeaveRequestRepository leaveRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userRepository.findByEmail(auth.getName());

        if (user == null)
            return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("myTasks", taskAssignmentRepository.findByUser(user));

        // Bổ sung thêm để nhân viên xem được lương và đơn nghỉ của mình
        model.addAttribute("myPayrolls", payrollRepository.findByUser(user));
        model.addAttribute("myLeaves", leaveRepository.findByUser(user));

        return "user1/dashboard";
    }
}