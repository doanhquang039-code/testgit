package com.example.hr.controllers;

import com.example.hr.models.JobPosting;
import com.example.hr.service.JobPostingService;
import com.example.hr.service.DepartmentService;
import com.example.hr.service.JobPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hiring/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final DepartmentService departmentService;
    private final JobPositionService jobPositionService;

    @GetMapping
    public String listJobPostings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Model model) {
        
        var jobs = (search != null && !search.trim().isEmpty()) 
                ? jobPostingService.searchJobPostings(search)
                : (status != null && !status.trim().isEmpty())
                    ? jobPostingService.getJobPostingsByStatus(status)
                    : jobPostingService.getAllJobPostings();
        
        var statistics = jobPostingService.getJobPostingStatistics();
        
        model.addAttribute("jobs", jobs);
        model.addAttribute("statistics", statistics);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("searchKeyword", search);
        
        return "hiring/jobs/list";
    }

    @GetMapping("/create")
    public String createJobForm(Model model) {
        model.addAttribute("job", new JobPosting());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("positions", jobPositionService.getAllPositions());
        return "hiring/jobs/create";
    }

    @PostMapping("/create")
    public String createJob(@ModelAttribute JobPosting job, RedirectAttributes redirectAttributes) {
        try {
            jobPostingService.createJobPosting(job);
            redirectAttributes.addFlashAttribute("successMessage", "Job posting created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating job posting: " + e.getMessage());
        }
        return "redirect:/hiring/jobs";
    }

    @GetMapping("/{id}")
    public String viewJob(@PathVariable Integer id, Model model) {
        var job = jobPostingService.getJobPostingById(id);
        jobPostingService.incrementViewCount(id);
        model.addAttribute("job", job);
        return "hiring/jobs/view";
    }

    @GetMapping("/{id}/edit")
    public String editJobForm(@PathVariable Integer id, Model model) {
        var job = jobPostingService.getJobPostingById(id);
        model.addAttribute("job", job);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("positions", jobPositionService.getAllPositions());
        return "hiring/jobs/edit";
    }

    @PostMapping("/{id}/edit")
    public String editJob(@PathVariable Integer id, @ModelAttribute JobPosting job, 
                         RedirectAttributes redirectAttributes) {
        try {
            jobPostingService.updateJobPosting(id, job);
            redirectAttributes.addFlashAttribute("successMessage", "Job posting updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating job posting: " + e.getMessage());
        }
        return "redirect:/hiring/jobs/" + id;
    }

    @PostMapping("/{id}/publish")
    public String publishJob(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            jobPostingService.publishJobPosting(id);
            redirectAttributes.addFlashAttribute("successMessage", "Job posting published successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error publishing job posting: " + e.getMessage());
        }
        return "redirect:/hiring/jobs/" + id;
    }

    @PostMapping("/{id}/close")
    public String closeJob(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            jobPostingService.closeJobPosting(id);
            redirectAttributes.addFlashAttribute("successMessage", "Job posting closed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error closing job posting: " + e.getMessage());
        }
        return "redirect:/hiring/jobs/" + id;
    }

    @GetMapping("/closing-soon")
    public String jobsClosingSoon(Model model) {
        var jobs = jobPostingService.getJobsClosingSoon(7); // Jobs closing in next 7 days
        model.addAttribute("jobs", jobs);
        return "hiring/jobs/closing-soon";
    }
}