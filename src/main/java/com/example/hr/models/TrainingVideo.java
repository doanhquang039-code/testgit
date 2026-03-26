package com.example.hr.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data

@Table(name = "training_video")
public class TrainingVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    
    @Column(name = "video_url")
    private String videoUrl;

    // THÊM DÒNG NÀY VÀO ĐỂ HẾT LỖI
    private String category; 

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    // --- Đừng quên thêm Getter và Setter cho category ---
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    // ... các getter/setter khác giữ nguyên
}