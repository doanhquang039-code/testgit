package com.example.hr.controllers;

import com.example.hr.enums.ShiftAssignmentStatus;
import com.example.hr.models.ShiftAssignment;
import com.example.hr.models.User;
import com.example.hr.models.WorkShift;
import com.example.hr.repository.UserRepository;
import com.example.hr.service.AuthUserHelper;
import com.example.hr.service.WorkShiftService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class WorkShiftController {

    private final WorkShiftService workShiftService;
    private final UserRepository userRepository;
    private final AuthUserHelper authUserHelper;

    public WorkShiftController(WorkShiftService workShiftService,
                                UserRepository userRepository,
                                AuthUserHelper authUserHelper) {
        this.workShiftService = workShiftService;
        this.userRepository = userRepository;
        this.authUserHelper = authUserHelper;
    }

    // ==================== ADMIN: WORK SHIFTS ====================

    @GetMapping("/admin/shifts")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String shiftList(Model model) {
        model.addAttribute("shifts", workShiftService.findAllShifts());
        return "admin/shift-list";
    }

    @GetMapping("/admin/shifts/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddShift(Model model) {
        model.addAttribute("shift", new WorkShift());
        return "admin/shift-form";
    }

    @GetMapping("/admin/shifts/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditShift(@PathVariable Integer id, Model model) {
        WorkShift shift = workShiftService.findShiftById(id)
                .orElseThrow(() -> new RuntimeException("Ca làm việc không tồn tại"));
        model.addAttribute("shift", shift);
        return "admin/shift-form";
    }

    @PostMapping("/admin/shifts/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveShift(@ModelAttribute WorkShift shift, RedirectAttributes ra) {
        workShiftService.saveShift(shift);
        ra.addFlashAttribute("success", "Lưu ca làm việc thành công!");
        return "redirect:/admin/shifts";
    }

    @GetMapping("/admin/shifts/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteShift(@PathVariable Integer id, RedirectAttributes ra) {
        workShiftService.deleteShift(id);
        ra.addFlashAttribute("success", "Đã xóa ca làm việc!");
        return "redirect:/admin/shifts";
    }

    // ==================== ADMIN: SHIFT ASSIGNMENTS ====================

    @GetMapping("/admin/shift-assignments")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String assignmentList(@RequestParam(required = false) Integer userId,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                  Model model) {
        LocalDate dateFrom = from != null ? from : LocalDate.now().withDayOfMonth(1);
        LocalDate dateTo   = to   != null ? to   : LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<ShiftAssignment> assignments;
        if (userId != null) {
            assignments = workShiftService.findAssignmentsByUserAndDateRange(userId, dateFrom, dateTo);
        } else {
            assignments = workShiftService.findAssignmentsByDateRange(dateFrom, dateTo);
        }

        model.addAttribute("assignments", assignments);
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("shifts", workShiftService.findActiveShifts());
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("from", dateFrom);
        model.addAttribute("to", dateTo);
        return "admin/shift-assignment-list";
    }

    @PostMapping("/admin/shift-assignments/save")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String saveAssignment(@RequestParam Integer userId,
                                  @RequestParam Integer shiftId,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDate,
                                  @RequestParam(required = false) String note,
                                  Authentication auth,
                                  RedirectAttributes ra) {
        // Kiểm tra trùng
        if (workShiftService.findAssignmentByUserAndDate(userId, workDate).isPresent()) {
            ra.addFlashAttribute("error", "Nhân viên này đã có ca trong ngày " + workDate);
            return "redirect:/admin/shift-assignments";
        }

        User user = userRepository.findById(userId).orElseThrow();
        WorkShift shift = workShiftService.findShiftById(shiftId).orElseThrow();
        User assigner = authUserHelper.getCurrentUser(auth);

        ShiftAssignment assignment = new ShiftAssignment();
        assignment.setUser(user);
        assignment.setShift(shift);
        assignment.setWorkDate(workDate);
        assignment.setNote(note);
        assignment.setStatus(ShiftAssignmentStatus.SCHEDULED);
        assignment.setAssignedBy(assigner);

        workShiftService.saveAssignment(assignment);
        ra.addFlashAttribute("success", "Phân ca thành công!");
        return "redirect:/admin/shift-assignments";
    }

    @GetMapping("/admin/shift-assignments/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String deleteAssignment(@PathVariable Integer id, RedirectAttributes ra) {
        workShiftService.deleteAssignment(id);
        ra.addFlashAttribute("success", "Đã xóa phân ca!");
        return "redirect:/admin/shift-assignments";
    }

    // ==================== USER: XEM CA LÀM VIỆC ====================

    @GetMapping("/user1/my-shifts")
    @PreAuthorize("isAuthenticated()")
    public String myShifts(@RequestParam(required = false) Integer month,
                            @RequestParam(required = false) Integer year,
                            Authentication auth, Model model) {
        User currentUser = authUserHelper.getCurrentUser(auth);
        if (currentUser == null) return "redirect:/login";

        int m = month != null ? month : LocalDate.now().getMonthValue();
        int y = year  != null ? year  : LocalDate.now().getYear();
        LocalDate from = LocalDate.of(y, m, 1);
        LocalDate to   = from.withDayOfMonth(from.lengthOfMonth());

        List<ShiftAssignment> myShifts = workShiftService
                .findAssignmentsByUserAndDateRange(currentUser.getId(), from, to);

        // Ca hôm nay
        ShiftAssignment todayShift = workShiftService
                .findAssignmentByUserAndDate(currentUser.getId(), LocalDate.now())
                .orElse(null);

        model.addAttribute("myShifts", myShifts);
        model.addAttribute("todayShift", todayShift);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("month", m);
        model.addAttribute("year", y);
        return "user1/my-shifts";
    }
}
