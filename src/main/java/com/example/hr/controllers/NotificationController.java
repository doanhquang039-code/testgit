package com.example.hr.controllers;

import com.example.hr.models.Notification;
import com.example.hr.models.User;
import com.example.hr.repository.UserRepository;
import com.example.hr.service.AuthUserHelper;
import com.example.hr.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired private NotificationService notificationService;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthUserHelper authUserHelper;

    private User getCurrentUser(Authentication auth) {
        return authUserHelper.getCurrentUser(auth);
    }

    @GetMapping
    public String listNotifications(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        if (user == null) return "redirect:/login";

        List<Notification> notifications = notificationService.getAll(user);
        long unreadCount = notificationService.countUnread(user);

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("user", user);
        return "user1/notifications";
    }

    @PostMapping("/mark-all-read")
    public String markAllRead(Authentication auth) {
        User user = getCurrentUser(auth);
        if (user != null) {
            notificationService.markAllRead(user);
        }
        return "redirect:/notifications";
    }

    @GetMapping("/read/{id}")
    public String markRead(@PathVariable Integer id, Authentication auth) {
        notificationService.markRead(id);
        return "redirect:/notifications";
    }

    /**
     * API endpoint to get unread count (for AJAX badge update)
     */
    @GetMapping("/count")
    @ResponseBody
    public long getUnreadCount(Authentication auth) {
        User user = getCurrentUser(auth);
        if (user == null) return 0;
        return notificationService.countUnread(user);
    }

    /**
     * API endpoint to get notification list (for dropdown panel)
     */
    @GetMapping("/api/list")
    @ResponseBody
    public List<Notification> getNotificationList(
            @RequestParam(defaultValue = "10") int limit,
            Authentication auth) {
        User user = getCurrentUser(auth);
        if (user == null) return List.of();
        return notificationService.getAll(user).stream()
                .limit(limit)
                .toList();
    }

    /**
     * API mark all read (AJAX)
     */
    @PutMapping("/api/mark-all-read")
    @ResponseBody
    public java.util.Map<String, Object> markAllReadApi(Authentication auth) {
        User user = getCurrentUser(auth);
        if (user != null) notificationService.markAllRead(user);
        return java.util.Map.of("success", true);
    }
}
