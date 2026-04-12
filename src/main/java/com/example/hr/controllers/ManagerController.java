package com.example.hr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.hr.repository.*;
import com.example.hr.enums.LeaveStatus;
import com.example.hr.enums.TaskStatus;
import com.example.hr.enums.UserStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired private LeaveRequestRepository leaveRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private TaskAssignmentRepository taskAssignmentRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Stats
        long totalEmployees = userRepository.findByStatus(UserStatus.ACTIVE).size();
        long pendingLeaves  = leaveRepository.countByStatus(LeaveStatus.PENDING);

        // Active tasks = assignments where status is IN_PROGRESS  
        long activeTasks = taskAssignmentRepository.findAllWithUser().stream()
                .filter(a -> a.getStatus() == TaskStatus.IN_PROGRESS).count();

        // Absent today = employees who haven't checked in today
        long checkedInToday = attendanceRepository.findByAttendanceDateBetween(
                LocalDate.now(), LocalDate.now()).size();
        long absentToday = Math.max(0, totalEmployees - checkedInToday);

        // Recent leaves (all, sorted by pending first)
        var allLeaves = leaveRepository.findAllWithUser(null);
        var pendingFirst = allLeaves.stream()
                .sorted((a, b) -> {
                    if (a.getStatus() == LeaveStatus.PENDING && b.getStatus() != LeaveStatus.PENDING) return -1;
                    if (a.getStatus() != LeaveStatus.PENDING && b.getStatus() == LeaveStatus.PENDING) return 1;
                    return 0;
                }).collect(java.util.stream.Collectors.toList());

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("pendingLeaves", pendingLeaves);
        model.addAttribute("activeTasks", activeTasks);
        model.addAttribute("absentToday", absentToday);
        model.addAttribute("recentLeaves", pendingFirst);
        model.addAttribute("recentTasks", taskRepository.findAll());
        model.addAttribute("teamMembers", userRepository.findByStatus(UserStatus.ACTIVE));
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")));

        return "manager/dashboard";
    }
}