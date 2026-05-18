package com.example.hr.controllers;

import com.example.hr.service.SystemSettingService;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("loginVerificationEnabled",
                settingService.getBoolean("LOGIN_VERIFICATION_ENABLED", true));
        model.addAttribute("loginVerificationCodeLength",
                settingService.getInt("LOGIN_VERIFICATION_CODE_LENGTH", 5, 4, 8));
        model.addAttribute("loginVerificationCharacters",
                settingService.getValue("LOGIN_VERIFICATION_CHARACTERS", "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"));
        return "admin/settings-list";
    }

    @PostMapping("/update")
    public String saveSettings(@RequestParam Map<String, String> params, RedirectAttributes ra) {
        params.entrySet().stream()
                .filter(entry -> !entry.getKey().startsWith("_"))
                .forEach(entry -> settingService.updateSetting(entry.getKey(), entry.getValue()));
        ra.addFlashAttribute("successMsg", "Cập nhật cấu hình hệ thống thành công.");
        return "redirect:/admin/settings";
    }

    @PostMapping("/add")
    public String addSetting(@RequestParam String settingKey,
                             @RequestParam String settingValue,
                             @RequestParam(required = false) String description,
                             RedirectAttributes ra) {
        String formattedKey = settingKey.trim().toUpperCase().replaceAll("\\s+", "_");
        settingService.addSetting(formattedKey, settingValue, description);
        ra.addFlashAttribute("successMsg", "Đã thêm cấu hình mới: " + formattedKey);
        return "redirect:/admin/settings";
    }
}
