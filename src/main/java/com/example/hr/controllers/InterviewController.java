package com.example.hr.controllers;

import com.example.hr.models.Interview;
import com.example.hr.service.InterviewService;
import com.example.hr.service.CandidateService;
import com.example.hr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/hiring/interviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER')")
public class InterviewController {

    private final InterviewService interviewService;
    private final CandidateService candidateService;
    private final UserService userService;

    @GetMapping
    public String listInterviews(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            Model model) {
        
        var interviews = (status != null && !status.trim().isEmpty())
                ? interviewService.getInterviewsByStatus(status)
                : interviewService.getAllInterviews();
        
        var statistics = interviewService.getInterviewStatistics();
        
        model.addAttribute("interviews", interviews);
        model.addAttribute("statistics", statistics);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedType", type);
        
        return "hiring/interviews/list";
    }

    @GetMapping("/create")
    public String createInterviewForm(@RequestParam(required = false) Integer candidateId, Model model) {
        var interview = new Interview();
        if (candidateId != null) {
            var candidate = candidateService.getCandidateById(candidateId);
            interview.setCandidate(candidate);
        }
        
        model.addAttribute("interview", interview);
        model.addAttribute("candidates", candidateService.getCandidatesByStage("INTERVIEW"));
        model.addAttribute("interviewers", userService.getUsersByRole("MANAGER"));
        
        return "hiring/interviews/create";
    }

    @PostMapping("/create")
    public String createInterview(@ModelAttribute Interview interview, RedirectAttributes redirectAttributes) {
        try {
            interviewService.scheduleInterview(interview);
            redirectAttributes.addFlashAttribute("successMessage", "Interview scheduled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error scheduling interview: " + e.getMessage());
        }
        return "redirect:/hiring/interviews";
    }

    @GetMapping("/{id}")
    public String viewInterview(@PathVariable Integer id, Model model) {
        var interview = interviewService.getInterviewById(id);
        model.addAttribute("interview", interview);
        return "hiring/interviews/view";
    }

    @GetMapping("/{id}/edit")
    public String editInterviewForm(@PathVariable Integer id, Model model) {
        var interview = interviewService.getInterviewById(id);
        model.addAttribute("interview", interview);
        model.addAttribute("candidates", candidateService.getAllCandidates());
        model.addAttribute("interviewers", userService.getUsersByRole("MANAGER"));
        return "hiring/interviews/edit";
    }

    @PostMapping("/{id}/edit")
    public String editInterview(@PathVariable Integer id, @ModelAttribute Interview interview, 
                               RedirectAttributes redirectAttributes) {
        try {
            interviewService.updateInterview(id, interview);
            redirectAttributes.addFlashAttribute("successMessage", "Interview updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating interview: " + e.getMessage());
        }
        return "redirect:/hiring/interviews/" + id;
    }

    @GetMapping("/{id}/feedback")
    public String feedbackForm(@PathVariable Integer id, Model model) {
        var interview = interviewService.getInterviewById(id);
        model.addAttribute("interview", interview);
        return "hiring/interviews/feedback";
    }

    @PostMapping("/{id}/feedback")
    public String submitFeedback(@PathVariable Integer id,
                                @RequestParam String feedback,
                                @RequestParam Integer technicalScore,
                                @RequestParam Integer communicationScore,
                                @RequestParam Integer culturalFitScore,
                                @RequestParam String recommendation,
                                RedirectAttributes redirectAttributes) {
        try {
            interviewService.completeInterview(id, feedback, technicalScore, 
                    communicationScore, culturalFitScore, recommendation);
            redirectAttributes.addFlashAttribute("successMessage", "Interview feedback submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting feedback: " + e.getMessage());
        }
        return "redirect:/hiring/interviews/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelInterview(@PathVariable Integer id, @RequestParam String reason, 
                                 RedirectAttributes redirectAttributes) {
        try {
            interviewService.cancelInterview(id, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Interview cancelled!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error cancelling interview: " + e.getMessage());
        }
        return "redirect:/hiring/interviews/" + id;
    }

    @PostMapping("/{id}/no-show")
    public String markNoShow(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            interviewService.markNoShow(id);
            redirectAttributes.addFlashAttribute("successMessage", "Interview marked as no-show!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error marking no-show: " + e.getMessage());
        }
        return "redirect:/hiring/interviews/" + id;
    }

    @GetMapping("/upcoming")
    public String upcomingInterviews(Model model) {
        // This would need current user context - simplified for now
        var upcomingInterviews = interviewService.getInterviewsByDateRange(
                LocalDateTime.now(), 
                LocalDateTime.now().plusWeeks(2)
        );
        
        model.addAttribute("interviews", upcomingInterviews);
        return "hiring/interviews/upcoming";
    }
}