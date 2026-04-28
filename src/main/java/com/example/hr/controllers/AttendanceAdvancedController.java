package com.example.hr.controllers;

import com.example.hr.models.*;
import com.example.hr.service.AdvancedAttendanceService;
import com.example.hr.service.AuthUserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/attendance/advanced")
@RequiredArgsConstructor
public class AttendanceAdvancedController {
    
    private final AdvancedAttendanceService attendanceService;
    private final AuthUserHelper authUserHelper;
    
    // ===== Geofencing Check-in =====
    
    @GetMapping("/checkin")
    @PreAuthorize("isAuthenticated()")
    public String checkinPage(Model model) {
        model.addAttribute("locations", attendanceService.getActiveLocations());
        return "attendance/geofencing-checkin";
    }
    
    @PostMapping("/checkin/validate")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public boolean validateLocation(@RequestParam Double latitude,
                                    @RequestParam Double longitude,
                                    @RequestParam Integer locationId) {
        return attendanceService.validateGeofencing(latitude, longitude, locationId);
    }
    
    // ===== Face Recognition =====
    
    @GetMapping("/face-setup")
    @PreAuthorize("isAuthenticated()")
    public String faceSetupPage(Authentication auth, Model model) {
        User user = authUserHelper.getCurrentUser(auth);
        model.addAttribute("hasFaceData", attendanceService.hasFaceRecognition(user));
        return "attendance/face-setup";
    }
    
    @PostMapping("/face-register")
    @PreAuthorize("isAuthenticated()")
    public String registerFace(@RequestParam String faceEncoding,
                              @RequestParam String imageUrl,
                              Authentication auth,
                              RedirectAttributes ra) {
        try {
            User user = authUserHelper.getCurrentUser(auth);
            attendanceService.registerFaceData(user, faceEncoding, imageUrl);
            ra.addFlashAttribute("success", "Đăng ký khuôn mặt thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/attendance/advanced/face-setup";
    }
    
    // ===== Shift Management (Admin) =====
    
    @GetMapping("/admin/shifts")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String shiftList(Model model) {
        model.addAttribute("shifts", attendanceService.getActiveShifts());
        return "attendance/admin/shift-list";
    }
    
    @GetMapping("/admin/shift/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String newShiftForm(Model model) {
        model.addAttribute("shift", new Shift());
        return "attendance/admin/shift-form";
    }
    
    @PostMapping("/admin/shift/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String saveShift(@ModelAttribute Shift shift, RedirectAttributes ra) {
        try {
            attendanceService.createShift(shift);
            ra.addFlashAttribute("success", "Tạo ca làm việc thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/attendance/advanced/admin/shifts";
    }
    
    // ===== Location Management (Admin) =====
    
    @GetMapping("/admin/locations")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String locationList(Model model) {
        model.addAttribute("locations", attendanceService.getActiveLocations());
        return "attendance/admin/location-list";
    }
    
    @PostMapping("/admin/location/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String saveLocation(@RequestParam String name,
                              @RequestParam Double latitude,
                              @RequestParam Double longitude,
                              @RequestParam Integer radiusMeters,
                              @RequestParam(required = false) String address,
                              RedirectAttributes ra) {
        try {
            attendanceService.createLocation(name, latitude, longitude, radiusMeters, address);
            ra.addFlashAttribute("success", "Tạo địa điểm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/attendance/advanced/admin/locations";
    }
    
    // ===== My Shift Schedule =====
    
    @GetMapping("/my-schedule")
    @PreAuthorize("isAuthenticated()")
    public String mySchedule(@RequestParam(required = false) String month,
                            Authentication auth,
                            Model model) {
        User user = authUserHelper.getCurrentUser(auth);
        LocalDate now = LocalDate.now();
        LocalDate startDate = month != null ? LocalDate.parse(month + "-01") : now.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        var assignments = attendanceService.getUserShiftAssignments(user, startDate, endDate);
        
        model.addAttribute("assignments", assignments);
        model.addAttribute("currentMonth", startDate);
        
        return "attendance/my-schedule";
    }
}
