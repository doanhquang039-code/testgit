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
        return "engagement/social-feed";
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
        return "engagement/recognition-wall";
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
        return "engagement/surveys";
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
        return "engagement/my-referrals";
    }
}
