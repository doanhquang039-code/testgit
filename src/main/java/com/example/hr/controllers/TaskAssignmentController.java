package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hr.enums.NotificationType;
import com.example.hr.enums.UserStatus;
import com.example.hr.models.TaskAssignment;
import com.example.hr.repository.TaskAssignmentRepository;
import com.example.hr.repository.TaskRepository;
import com.example.hr.repository.UserRepository;
import com.example.hr.service.NotificationService;
import org.springframework.transaction.annotation.Transactional;
@Controller
@RequestMapping("/admin/assignments")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentRepository assignmentRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;

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
        assignmentRepository.save(assignment);
        // Notify employee about new task
        if (assignment.getUser() != null && assignment.getTask() != null) {
            notificationService.createNotification(
                assignment.getUser(),
                "📝 Bạn được giao công việc mới: \"" + assignment.getTask().getTaskName() + "\". Hãy xắtỷ buổi hôm nay!",
                NotificationType.TASK_ASSIGNED,
                "/user1/tasks"
            );
        }
        return "redirect:/admin/assignments";
    }

    // 4. Xóa phân công (Thu hồi việc)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        assignmentRepository.deleteById(id);
        return "redirect:/admin/assignments";
    }
}
