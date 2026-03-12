package com.example.hr.controllers;

import com.example.hr.models.User;
import com.example.hr.enums.UserStatus;
import com.example.hr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hiring")
public class HiringController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listHiring(Model model) {
        // Lấy danh sách nhân viên đang chờ duyệt
        // Trong HiringController
        model.addAttribute("newHires", userRepository.findByStatus(UserStatus.INACTIVE)); // Sửa PENDING thành tên đúng
                                                                                          // trong Enum của bạn
        return "hiring/dashboard";
    }

    @GetMapping("/activate/{id}")
    public String activateUser(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID nhân viên không tồn tại: " + id));

        // Chuyển trạng thái sang ACTIVE
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // QUAN TRỌNG: Dùng redirect để trình duyệt tải lại danh sách mới
        return "redirect:/hiring";
    }
}