package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hr.models.JobPosition;
import com.example.hr.repository.JobPositionRepository;
@Controller
@RequestMapping("/admin/positions")
public class JobPositionController {
    
    @Autowired
    private JobPositionRepository positionRepository;

    // Hiển thị danh sách chức vụ
    @GetMapping
    public String list(Model model) {
        // Chỉ lấy những chức vụ chưa bị xóa để hiển thị lên bảng
        model.addAttribute("positions", positionRepository.findByActiveTrue());
        return "admin/position-list";
    }

    // Hiển thị form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("position", new JobPosition());
        // QUAN TRỌNG: Phải có dòng này để lấy danh sách chức vụ cho ô Select
        model.addAttribute("allPositions", positionRepository.findByActiveTrue());
        return "admin/position-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        JobPosition pos = positionRepository.findById(id).orElseThrow();
        model.addAttribute("position", pos);
        // CHỈ lấy những thằng đang Active để chọn làm mốc thăng tiến
        model.addAttribute("allPositions", positionRepository.findByActiveTrue()); 
        return "admin/position-form";
    }

    // Lưu dữ liệu (dùng chung cho cả thêm và sửa)
    @PostMapping("/save")
    public String save(@ModelAttribute("position") JobPosition pos) {
        positionRepository.save(pos);
        return "redirect:/admin/positions";
    }

    // Xóa chức vụ
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        JobPosition pos = positionRepository.findById(id).orElseThrow();
        
        // Thay vì positionRepository.deleteById(id);
        pos.setActive(false); // Chuyển sang trạng thái "đã xóa"
        positionRepository.save(pos);
        
        return "redirect:/admin/positions";
    }
}