package com.example.hr.repository;

import com.example.hr.models.ChatbotMessage;
import com.example.hr.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatbotMessageRepository extends JpaRepository<ChatbotMessage, Long> {
    // Tìm kiếm lịch sử chat theo User
    List<ChatbotMessage> findByUser(User user);
}