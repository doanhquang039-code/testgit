package com.example.hr.controllers;

import com.example.hr.models.User;
import com.example.hr.repository.*;

import java.security.Principal;

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

   @GetMapping("/dashboard") // Hoặc endpoint tương ứng của bạn
public String dashboard(Principal principal, Model model) {
    // Lấy username từ người dùng đang đăng nhập
    String username = principal.getName(); 
    
    // Sau đó mới dùng biến username này để tìm kiếm và dùng .orElse(null)
    User user = userRepository.findByUsername(username).orElse(null);
    
    model.addAttribute("user", user);
    return "user/dashboard";
}
}