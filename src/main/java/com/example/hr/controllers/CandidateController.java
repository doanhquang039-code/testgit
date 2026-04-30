package com.example.hr.controllers;

import com.example.hr.models.Candidate;
import com.example.hr.models.JobPosting;
import com.example.hr.service.CandidateService;
import com.example.hr.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hiring/candidates")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
public class CandidateController {

    private final CandidateService candidateService;
    private final JobPostingService jobPostingService;

    @GetMapping
    public String listCandidates(
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) String search,
            Model model) {
        
        var candidates = (search != null && !search.trim().isEmpty()) 
                ? candidateService.searchCandidates(search)
                : (stage != null && !stage.trim().isEmpty())
                    ? candidateService.getCandidatesByStage(stage)
                    : candidateService.getAllCandidates();
        
        var statistics = candidateService.getCandidateStatistics();
        
        model.addAttribute("candidates", candidates);
        model.addAttribute("statistics", statistics);
        model.addAttribute("selectedStage", stage);
        model.addAttribute("searchKeyword", search);
        
        return "hiring/candidates/list";
    }

    @GetMapping("/create")
    public String createCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        model.addAttribute("jobPostings", jobPostingService.getActiveJobPostings());
        return "hiring/candidates/create";
    }

    @PostMapping("/create")
    public String createCandidate(@ModelAttribute Candidate candidate, RedirectAttributes redirectAttributes) {
        try {
            candidateService.createCandidate(candidate);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating candidate: " + e.getMessage());
        }
        return "redirect:/hiring/candidates";
    }

    @GetMapping("/{id}")
    public String viewCandidate(@PathVariable Integer id, Model model) {
        var candidate = candidateService.getCandidateById(id);
        model.addAttribute("candidate", candidate);
        return "hiring/candidates/view";
    }

    @GetMapping("/{id}/edit")
    public String editCandidateForm(@PathVariable Integer id, Model model) {
        var candidate = candidateService.getCandidateById(id);
        model.addAttribute("candidate", candidate);
        model.addAttribute("jobPostings", jobPostingService.getActiveJobPostings());
        return "hiring/candidates/edit";
    }

    @PostMapping("/{id}/edit")
    public String editCandidate(@PathVariable Integer id, @ModelAttribute Candidate candidate, 
                               RedirectAttributes redirectAttributes) {
        try {
            candidateService.updateCandidate(id, candidate);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating candidate: " + e.getMessage());
        }
        return "redirect:/hiring/candidates/" + id;
    }

    @PostMapping("/{id}/move-stage")
    public String moveToStage(@PathVariable Integer id, @RequestParam String stage, 
                             RedirectAttributes redirectAttributes) {
        try {
            candidateService.moveToStage(id, stage);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate moved to " + stage + " stage!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error moving candidate: " + e.getMessage());
        }
        return "redirect:/hiring/candidates/" + id;
    }

    @PostMapping("/{id}/score")
    public String updateScore(@PathVariable Integer id, @RequestParam Integer score, 
                             RedirectAttributes redirectAttributes) {
        try {
            candidateService.updateScore(id, score);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate score updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating score: " + e.getMessage());
        }
        return "redirect:/hiring/candidates/" + id;
    }

    @PostMapping("/{id}/notes")
    public String addNotes(@PathVariable Integer id, @RequestParam String notes, 
                          RedirectAttributes redirectAttributes) {
        try {
            candidateService.addNotes(id, notes);
            redirectAttributes.addFlashAttribute("successMessage", "Notes added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding notes: " + e.getMessage());
        }
        return "redirect:/hiring/candidates/" + id;
    }

    @PostMapping("/{id}/hire")
    public String hireCandidate(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            candidateService.hireCandidate(id);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate hired successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error hiring candidate: " + e.getMessage());
        }
        return "redirect:/hiring/candidates/" + id;
    }

    @PostMapping("/{id}/reject")
    public String rejectCandidate(@PathVariable Integer id, @RequestParam String reason, 
                                 RedirectAttributes redirectAttributes) {
        try {
            candidateService.rejectCandidate(id, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate rejected!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting candidate: " + e.getMessage());
        }
        return "redirect:/hiring/candidates/" + id;
    }
}