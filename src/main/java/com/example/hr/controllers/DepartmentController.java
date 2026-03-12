package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hr.models.Department;
import com.example.hr.repository.DepartmentRepository;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin/department-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "admin/department-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("department") Department dept) {
        departmentRepository.save(dept);
        return "redirect:/admin/departments";
    }
 // Thêm hàm này vào DepartmentController.java
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        // Tìm phòng ban theo ID, nếu không thấy thì báo lỗi hoặc quay lại danh sách
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID phòng ban không hợp lệ: " + id));
        
        // Đẩy dữ liệu phòng ban vào model để hiển thị lên form
        model.addAttribute("department", department);
        
        // Dùng chung form với phần "Add" để tiết kiệm thời gian
        return "admin/department-form"; 
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        departmentRepository.deleteById(id);
        return "redirect:/admin/departments";
    }
}