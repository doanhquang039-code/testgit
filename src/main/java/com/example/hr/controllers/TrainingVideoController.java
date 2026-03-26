package com.example.hr.controllers;

// Import các thư viện của Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Import các class trong project của bạn
import com.example.hr.repository.TrainingVideoRepository;
import com.example.hr.models.TrainingVideo;

@Controller
@RequestMapping("/videos")
public class TrainingVideoController {

    @Autowired
    private TrainingVideoRepository videoRepository;

    @GetMapping
    public String listVideos(Model model) {
        // Gửi danh sách video từ DB sang file HTML
        model.addAttribute("videos", videoRepository.findAll());
        return "video-list"; 
    }
}