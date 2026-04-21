package com.example.hr.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "training_video")
public class TrainingVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url", nullable = false, length = 500)
    private String videoUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    /** Cloudinary public_id — dùng để xóa video */
    @Column(name = "public_id", length = 300)
    private String publicId;

    @Column(length = 100)
    private String category;

    /** Thời lượng video (giây) */
    @Column(name = "duration_sec")
    private Integer durationSec = 0;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "is_published")
    private Boolean isPublished = true;

    /** Comma-separated tags */
    @Column(length = 500)
    private String tags;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Helpers ---

    /** Định dạng thời lượng: "mm:ss" hoặc "hh:mm:ss" */
    public String getFormattedDuration() {
        if (durationSec == null || durationSec <= 0) return "—";
        int h = durationSec / 3600;
        int m = (durationSec % 3600) / 60;
        int s = durationSec % 60;
        if (h > 0) return String.format("%d:%02d:%02d", h, m, s);
        return String.format("%d:%02d", m, s);
    }

    /** Lấy thumbnail, fallback về placeholder nếu null */
    public String getThumbnailOrDefault() {
        return (thumbnailUrl != null && !thumbnailUrl.isBlank())
                ? thumbnailUrl
                : "https://placehold.co/320x180/1e293b/94a3b8?text=Video";
    }

    /** Tags dạng array */
    public String[] getTagArray() {
        if (tags == null || tags.isBlank()) return new String[0];
        return tags.split(",");
    }
}
