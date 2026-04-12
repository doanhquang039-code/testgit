package com.example.hr.service;

import com.example.hr.enums.NotificationType;
import com.example.hr.models.Notification;
import com.example.hr.models.User;
import com.example.hr.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Tạo thông báo mới cho một user
     */
    public void createNotification(User user, String message, NotificationType type, String link) {
        Notification notif = new Notification();
        notif.setUser(user);
        notif.setMessage(message);
        notif.setType(type);
        notif.setLink(link);
        notif.setRead(false);
        notif.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notif);
    }

    /**
     * Tạo thông báo INFO đơn giản
     */
    public void notify(User user, String message) {
        createNotification(user, message, NotificationType.INFO, null);
    }

    /**
     * Lấy tất cả thông báo của user
     */
    public List<Notification> getAll(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Lấy thông báo chưa đọc
     */
    public List<Notification> getUnread(User user) {
        return notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user);
    }

    /**
     * Đếm số thông báo chưa đọc
     */
    public long countUnread(User user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    /**
     * Đánh dấu tất cả đã đọc
     */
    public void markAllRead(User user) {
        notificationRepository.markAllReadByUser(user);
    }

    /**
     * Đánh dấu một thông báo đã đọc
     */
    public void markRead(Integer id) {
        notificationRepository.markReadById(id);
    }
}
