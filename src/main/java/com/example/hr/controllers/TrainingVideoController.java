package com.example.hr.controllers;

import com.example.hr.models.TrainingVideo;
import com.example.hr.service.VideoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User-facing controller — xem thư viện video đào tạo.
 */
@Controller
@RequestMapping("/videos")
public class TrainingVideoController {

    private final VideoService videoService;

    public TrainingVideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    /** Thư viện video — tất cả mọi người đều xem được */
    @GetMapping
    public String library(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String category,
                           Model model) {
        List<TrainingVideo> videos = videoService.searchVideos(keyword, category);
        model.addAttribute("videos", videos);
        model.addAttribute("categories", videoService.findDistinctCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        return "videos/library";
    }

    /** Xem chi tiết + player */
    @GetMapping("/{id}")
    public String watch(@PathVariable Integer id, Model model) {
        TrainingVideo video = videoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Video không tồn tại"));

        if (!Boolean.TRUE.equals(video.getIsPublished())) {
            return "redirect:/videos";
        }

        // Tăng lượt xem
        videoService.incrementView(id);

        // Video liên quan (cùng category)
        List<TrainingVideo> related = videoService.searchVideos(null, video.getCategory())
                .stream()
                .filter(v -> !v.getId().equals(id))
                .limit(6)
                .toList();

        model.addAttribute("video", video);
        model.addAttribute("related", related);
        return "videos/watch";
    }
}
