package com.example.hr.controllers;

import com.example.hr.enums.AssetStatus;
import com.example.hr.enums.UserStatus;
import com.example.hr.models.AssetAssignment;
import com.example.hr.models.CompanyAsset;
import com.example.hr.models.User;
import com.example.hr.repository.AssetAssignmentRepository;
import com.example.hr.repository.CompanyAssetRepository;
import com.example.hr.repository.UserRepository;
import com.example.hr.service.AuthUserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/assets")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class AssetController {

    @Autowired private CompanyAssetRepository assetRepository;
    @Autowired private AssetAssignmentRepository assignmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthUserHelper authUserHelper;

    @GetMapping
    public String listAssets(@RequestParam(required = false) String keyword, Model model) {
        List<CompanyAsset> assets;
        if (keyword != null && !keyword.isBlank()) {
            assets = assetRepository.findByAssetNameContainingIgnoreCaseOrAssetCodeContainingIgnoreCase(keyword, keyword);
        } else {
            assets = assetRepository.findAll();
        }
        model.addAttribute("assets", assets);
        model.addAttribute("keyword", keyword);
        return "admin/asset-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("asset", new CompanyAsset());
        model.addAttribute("statuses", AssetStatus.values());
        return "admin/asset-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        CompanyAsset asset = assetRepository.findById(id).orElseThrow();
        model.addAttribute("asset", asset);
        model.addAttribute("statuses", AssetStatus.values());
        return "admin/asset-form";
    }

    @PostMapping("/save")
    public String saveAsset(@ModelAttribute CompanyAsset asset, RedirectAttributes ra) {
        assetRepository.save(asset);
        ra.addFlashAttribute("successMsg", "Lưu tài sản thành công!");
        return "redirect:/admin/assets";
    }

    @GetMapping("/delete/{id}")
    public String deleteAsset(@PathVariable Integer id, RedirectAttributes ra) {
        assetRepository.deleteById(id);
        ra.addFlashAttribute("successMsg", "Đã xóa tài sản!");
        return "redirect:/admin/assets";
    }

    // ==================== QUẢN LÝ CẤP PHÁT (ASSIGNMENTS) ====================

    @GetMapping("/assignments")
    public String listAssignments(Model model) {
        model.addAttribute("assignments", assignmentRepository.findAll());
        return "admin/asset-assignment-list";
    }

    @GetMapping("/assign")
    public String showAssignForm(@RequestParam(required = false) Integer assetId, Model model) {
        model.addAttribute("assignment", new AssetAssignment());
        model.addAttribute("users", userRepository.findByStatus(UserStatus.ACTIVE));
        model.addAttribute("assets", assetRepository.findByStatus(AssetStatus.AVAILABLE));
        if (assetId != null) {
            model.addAttribute("selectedAssetId", assetId);
        }
        return "admin/asset-assign-form";
    }

    @PostMapping("/assign/save")
    public String saveAssignment(@ModelAttribute AssetAssignment assignment,
                                 @RequestParam Integer assetId,
                                 @RequestParam Integer userId,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        User assignedBy = authUserHelper.getCurrentUser(auth);
        CompanyAsset asset = assetRepository.findById(assetId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        assignment.setAsset(asset);
        assignment.setUser(user);
        assignment.setAssignedBy(assignedBy);
        if (assignment.getAssignedDate() == null) {
            assignment.setAssignedDate(LocalDate.now());
        }

        asset.markAssigned();
        assetRepository.save(asset);
        assignmentRepository.save(assignment);

        ra.addFlashAttribute("successMsg", "Đã cấp phát tài sản cho nhân viên!");
        return "redirect:/admin/assets/assignments";
    }

    @PostMapping("/return/{id}")
    public String returnAsset(@PathVariable Integer id,
                              @RequestParam String conditionOnReturn,
                              RedirectAttributes ra) {
        AssetAssignment assignment = assignmentRepository.findById(id).orElseThrow();
        
        // Hàm này trong Model của bạn đã có sẵn logic markAvailable() cho Asset 
        // và set thời gian thu hồi
        assignment.returnAsset(conditionOnReturn); 
        
        CompanyAsset asset = assignment.getAsset();
        assetRepository.save(asset);
        assignmentRepository.save(assignment);

        ra.addFlashAttribute("successMsg", "Đã thu hồi tài sản!");
        return "redirect:/admin/assets/assignments";
    }
}