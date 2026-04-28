package com.example.hr.controllers;

import com.example.hr.models.*;
import com.example.hr.service.AuthUserHelper;
import com.example.hr.service.EmployeeEngagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/engagement")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class EngagementController {
    
    private final EmployeeEngagementService engagementService;
    private final AuthUserHelper authUserHelper;
    
    // ===== Social Feed =====
    
    @GetMapping("/feed")
    public String socialFeed(Model model) {
        model.addAttribute("posts", engagementService.getPublicPosts());
        model.addAttribute("trending", engagementService.getTrendingPosts());
        return "user1/social-feed";
    }
    
    @PostMapping("/post/create")
    public String createPost(@RequestParam String content,
                            @RequestParam(required = false) String images,
                            @RequestParam(defaultValue = "POST") String type,
                            @RequestParam(defaultValue = "true") Boolean isPublic,
                            Authentication auth,
                            RedirectAttributes ra) {
        try {
            User user = authUserHelper.getCurrentUser(auth);
            engagementService.createPost(user, content, images, type, isPublic);
            ra.addFlashAttribute("success", "Đăng bài thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/engagement/feed";
    }
    
    @PostMapping("/post/{id}/like")
    @ResponseBody
    public int likePost(@PathVariable Integer id) {
        SocialPost post = engagementService.likePost(id);
        return post.getLikeCount();
    }
    
    // ===== Recognition =====
    
    @GetMapping("/recognition")
    public String recognitionWall(Model model) {
        model.addAttribute("recognitions", engagementService.getPublicRecognitions());
        return "user1/recognition-wall";
    }
    
    @GetMapping("/recognition/give")
    public String giveRecognitionForm(Model model) {
        return "engagement/give-recognition";
    }
    
    @PostMapping("/recognition/give")
    public String giveRecognition(@RequestParam Integer recipientId,
                                 @RequestParam String type,
                                 @RequestParam String title,
                                 @RequestParam String message,
                                 @RequestParam(defaultValue = "10") Integer points,
                                 @RequestParam(defaultValue = "true") Boolean isPublic,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        try {
            User giver = authUserHelper.getCurrentUser(auth);
            // Get recipient from UserRepository
            User recipient = new User();
            recipient.setId(recipientId);
            
            engagementService.giveRecognition(recipient, giver, type, title, message, points, isPublic);
            ra.addFlashAttribute("success", "Gửi lời khen thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/engagement/recognition";
    }
    
    @GetMapping("/my-points")
    public String myPoints(Authentication auth, Model model) {
        User user = authUserHelper.getCurrentUser(auth);
        
        model.addAttribute("totalPoints", engagementService.getUserTotalPoints(user));
        model.addAttribute("recognitions", engagementService.getUserRecognitions(user));
        
        return "engagement/my-points";
    }
    
    // ===== Surveys =====
    
    @GetMapping("/surveys")
    public String surveys(Model model) {
        model.addAttribute("surveys", engagementService.getActiveSurveys());
        return "user1/surveys";
    }
    
    @GetMapping("/survey/{id}")
    public String takeSurvey(@PathVariable Integer id, Model model) {
        // Get survey details
        model.addAttribute("surveyId", id);
        return "engagement/take-survey";
    }
    
    @PostMapping("/survey/{id}/submit")
    public String submitSurvey(@PathVariable Integer id,
                              @RequestParam String answers,
                              Authentication auth,
                              RedirectAttributes ra) {
        try {
            User user = authUserHelper.getCurrentUser(auth);
            PulseSurvey survey = new PulseSurvey();
            survey.setId(id);
            
            engagementService.submitResponse(survey, user, answers);
            ra.addFlashAttribute("success", "Cảm ơn bạn đã tham gia khảo sát!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/engagement/surveys";
    }
    
    // ===== Referrals =====
    
    @GetMapping("/referrals")
    public String myReferrals(Authentication auth, Model model) {
        User user = authUserHelper.getCurrentUser(auth);
        model.addAttribute("referrals", engagementService.getUserReferrals(user));
        return "user1/my-referrals";
    }
    
    // ===== ADMIN ROUTES =====
    
    @GetMapping("/admin/surveys")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminSurveyList(Model model) {
        model.addAttribute("surveys", engagementService.getAllSurveys());
        model.addAttribute("stats", engagementService.getSurveyStats());
        return "admin/survey-list";
    }
    
    @GetMapping("/admin/surveys/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminSurveyCreateForm(Model model) {
        model.addAttribute("survey", new PulseSurvey());
        return "admin/survey-form";
    }
    
    @PostMapping("/admin/surveys/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminSurveyCreate(@ModelAttribute PulseSurvey survey,
                                   Authentication auth,
                                   RedirectAttributes ra) {
        try {
            User createdBy = authUserHelper.getCurrentUser(auth);
            engagementService.createSurvey(
                survey.getTitle(),
                survey.getDescription(),
                survey.getQuestions(),
                survey.getStartDate(),
                survey.getEndDate(),
                survey.getIsAnonymous(),
                createdBy
            );
            ra.addFlashAttribute("success", "Tạo khảo sát thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/engagement/admin/surveys";
    }
    
    @GetMapping("/admin/surveys/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminSurveyEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("survey", engagementService.getSurveyById(id));
        return "admin/survey-form";
    }
    
    @PostMapping("/admin/surveys/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminSurveyUpdate(@PathVariable Integer id,
                                   @ModelAttribute PulseSurvey survey,
                                   RedirectAttributes ra) {
        try {
            engagementService.updateSurvey(id, survey);
            ra.addFlashAttribute("success", "Cập nhật khảo sát thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/engagement/admin/surveys";
    }
    
    @PostMapping("/admin/surveys/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminSurveyDelete(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            engagementService.deleteSurvey(id);
            ra.addFlashAttribute("success", "Xóa khảo sát thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/engagement/admin/surveys";
    }
    
    @GetMapping("/admin/recognition")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String adminRecognitionList(Model model) {
        model.addAttribute("recognitions", engagementService.getAllRecognitions());
        model.addAttribute("stats", engagementService.getRecognitionStats());
        return "admin/recognition-list";
    }
}
