package com.example.hr.controllers;

import com.example.hr.enums.Role;
import com.example.hr.models.User;
import com.example.hr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class SocialLoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login/social")
    public String socialLogin(@RequestParam String provider) {
        String mockEmail = provider + "_user@example.com";
        User user = userRepository.findByEmail(mockEmail); // Đã hết lỗi nhờ bước 1

        if (user == null) {
            user = new User();
            user.setEmail(mockEmail);
            user.setFullName(provider.toUpperCase() + " User");

            // Sửa l ỗi setRole bằng cách dùng Enum chính xác
            // Đảm bảo bạn đã import Enum Role vào file này
            user.setRole(Role.USER);

            userRepository.save(user);
        }

        return "redirect:/user1/dashboard";
    }
}