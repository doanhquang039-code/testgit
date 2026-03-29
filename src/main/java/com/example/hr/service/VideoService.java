package com.example.hr.service;

import com.example.hr.models.TrainingVideo;
import com.example.hr.repository.TrainingVideoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VideoService {

    private final TrainingVideoRepository videoRepository;

    public VideoService(TrainingVideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<TrainingVideo> searchVideos(String keyword, String category) {
        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasCategory = category != null && !category.isEmpty();

        if (hasKeyword && hasCategory) {
            return videoRepository
                .findByTitleContainingIgnoreCaseAndCategory(keyword, category);
        }
        if (hasKeyword) {
            Sort sort = Sort.by(Sort.Direction.ASC, "title");
            return videoRepository
                .findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    keyword, keyword, sort);
        }
        if (hasCategory) {
            return videoRepository.findByCategory(category);
        }
        return videoRepository.findAll();
    }
}