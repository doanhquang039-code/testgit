package com.example.hr.controllers;

import com.example.hr.models.TrainingVideo;
import com.example.hr.service.CloudinaryService;
import com.example.hr.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Controller
public class VideoController {

    // ✅ Khai báo field đầy đủ
    private final CloudinaryService cloudinaryService;
    private final VideoService videoService;

    // ✅ Constructor Injection — 1 constructor duy nhất
    public VideoController(CloudinaryService cloudinaryService,
                           VideoService videoService) {
        this.cloudinaryService = cloudinaryService;
        this.videoService = videoService;
    }

    @GetMapping("/videos/upload")
    public String uploadPage() {
        return "upload_video";
    }

    @PostMapping("/videos/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Vui lòng chọn một file video!");
            return "upload_video";
        }
        try {
            String videoUrl = cloudinaryService.uploadFile(file, "hr_videos");
            model.addAttribute("message", "Upload thành công!");
            model.addAttribute("videoUrl", videoUrl);
        } catch (IOException e) {
            model.addAttribute("message", "Lỗi upload: " + e.getMessage());
        }
        return "upload_video";
    }

    @GetMapping("/videos/list")
    public String listVideos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            Model model) {
        List<TrainingVideo> videos = videoService.searchVideos(search, category);
        model.addAttribute("videos", videos);
        return "video_list";
    }
}