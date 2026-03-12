package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hr.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Lấy tất cả nhân viên hiển thị lên dashboard
        model.addAttribute("users", userRepository.findAll());
        return "admin/dashboard"; 
    }
}