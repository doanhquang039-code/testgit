package com.example.hr.controllers;

import com.example.hr.service.SystemSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin/settings")
@PreAuthorize("hasRole('ADMIN')")
public class SystemSettingController {

    private final SystemSettingService settingService;

    public SystemSettingController(SystemSettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    public String listSettings(Model model) {
        model.addAttribute("settings", settingService.getAllSettings());
        return "admin/settings-list"; // Cần tạo file view giao diện này
    }

    @PostMapping("/update")
    public String saveSettings(@RequestParam Map<String, String> params, RedirectAttributes ra) {
        params.entrySet().stream()
              .filter(entry -> !entry.getKey().startsWith("_")) // Bỏ qua _csrf
              .forEach(entry -> settingService.updateSetting(entry.getKey(), entry.getValue()));
        ra.addFlashAttribute("successMsg", "✅ Cập nhật cấu hình hệ thống thành công!");
        return "redirect:/admin/settings";
    }

    @PostMapping("/add")
    public String addSetting(@RequestParam String settingKey,
                             @RequestParam String settingValue,
                             @RequestParam(required = false) String description,
                             RedirectAttributes ra) {
        String formattedKey = settingKey.trim().toUpperCase().replaceAll("\\s+", "_");
        settingService.addSetting(formattedKey, settingValue, description);
        ra.addFlashAttribute("successMsg", "✅ Đã thêm cấu hình mới: " + formattedKey);
        return "redirect:/admin/settings";
    }
}