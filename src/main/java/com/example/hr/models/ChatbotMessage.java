package com.example.hr.models; // 1. Đảm bảo dòng này khớp với thư mục

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chatbotmessage")
@Data
public class ChatbotMessage { // 2. Tên class PHẢI khớp chính xác với tên file
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "user_query", columnDefinition = "TEXT")
    private String userQuery;

    @Column(name = "bot_response", columnDefinition = "TEXT")
    private String botResponse;

    private String intent;
    private Integer rating;
    private Boolean isEscalated = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}