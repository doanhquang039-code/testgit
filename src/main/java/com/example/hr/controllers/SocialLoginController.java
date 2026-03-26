package com.example.hr.controllers;

import com.example.hr.enums.Role;
import com.example.hr.models.User;
import com.example.hr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SocialLoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login/social")
    public String socialLogin(@RequestParam String provider) {
        // 1. Tạo email giả định từ provider (google, facebook...)
        String mockEmail = provider + "_user@example.com";
        User user = userRepository.findByEmail(mockEmail).orElse(null);
      

        // 3. Nếu chưa có user thì tạo mới
        if (user == null) {
            user = new User();
            user.setEmail(mockEmail);
            user.setFullName(provider.toUpperCase() + " User");
            user.setUsername(provider + "_" + System.currentTimeMillis()); // Thêm username để tránh lỗi null
            user.setPassword("social_login_dummy"); // Password giả cho social login
            
            // Dùng Enum Role chính xác
            user.setRole(Role.USER);

            userRepository.save(user);
        }

        return "redirect:/user1/dashboard";
    }
}