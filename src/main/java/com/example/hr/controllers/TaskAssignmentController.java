package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hr.enums.UserStatus;
import com.example.hr.models.TaskAssignment;
import com.example.hr.repository.TaskAssignmentRepository;
import com.example.hr.repository.TaskRepository;
import com.example.hr.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
@Controller
@RequestMapping("/admin/assignments")
public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentRepository assignmentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Hiển thị danh sách phân công
    @GetMapping
    @Transactional(readOnly = true)
    public String list(Model model) {
        model.addAttribute("assignments", assignmentRepository.findAll());
        return "admin/assignment-list";
    }

    // 2. Form giao việc mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("assignment", new TaskAssignment());
        // Lấy danh sách Task và User để chọn từ Dropdown
        model.addAttribute("tasks", taskRepository.findAll());
        model.addAttribute("users", userRepository.findByStatus(UserStatus.ACTIVE));
        return "admin/assignment-form";
    }

    // 3. Lưu phân công
    @PostMapping("/save")
    public String save(@ModelAttribute("assignment") TaskAssignment assignment) {
        // Có thể thêm logic tự động gán ngày giao là ngày hiện tại
        assignmentRepository.save(assignment);
        return "redirect:/admin/assignments";
    }

    // 4. Xóa phân công (Thu hồi việc)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        assignmentRepository.deleteById(id);
        return "redirect:/admin/assignments";
    }
}