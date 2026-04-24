package com.example.hr.controllers;

import com.example.hr.models.Department;
import com.example.hr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "admin/department-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "admin/department-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("department") Department dept) {
        departmentService.saveDepartment(dept);
        return "redirect:/admin/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("department", departmentService.getDepartmentById(id));
        return "admin/department-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return "redirect:/admin/departments";
    }
}
