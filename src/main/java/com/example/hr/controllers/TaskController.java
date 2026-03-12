package com.example.hr.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hr.models.Task;
import com.example.hr.repository.TaskRepository;
import com.example.hr.repository.UserRepository;
import com.example.hr.enums.UserStatus;

@Controller
@RequestMapping("/admin/tasks")
public class TaskController {

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;

    // 1. Danh sách & Tìm kiếm công việc
    @GetMapping
    public String list(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Task> tasks;
        if (keyword != null && !keyword.isEmpty()) {
            tasks = taskRepository.findByTaskNameContaining(keyword); // Cần thêm hàm này vào Repo
        } else {
            tasks = taskRepository.findAll();
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        return "admin/task-list";
    }

    // 2. Form thêm mới công việc
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return "admin/task-form";
    }

    // 3. Form chỉnh sửa công việc
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID công việc không tồn tại: " + id));
        model.addAttribute("task", task);
        return "admin/task-form";
    }

    // 4. Lưu dữ liệu (Thêm/Sửa)
    @PostMapping("/save")
    public String save(@ModelAttribute("task") Task task) {
        taskRepository.save(task);
        return "redirect:/admin/tasks";
    }

    // 5. Xóa công việc
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        taskRepository.deleteById(id);
        return "redirect:/admin/tasks";
    }

    // 6. Giao việc (Phần bạn đã viết, bổ sung Model để hiện list User)
    @GetMapping("/assign/{id}")
    public String showAssignForm(@PathVariable Integer id, Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        model.addAttribute("task", task);
        model.addAttribute("users", userRepository.findByStatus(UserStatus.ACTIVE));
        return "admin/task-assign"; // Tạo trang riêng để chọn nhân viên
    }
}