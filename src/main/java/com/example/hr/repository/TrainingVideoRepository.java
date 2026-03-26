package com.example.hr.repository;

import com.example.hr.models.TrainingVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainingVideoRepository extends JpaRepository<TrainingVideo, Integer> {
    
    // Tìm kiếm video theo tiêu đề (phục vụ tính năng tìm kiếm sau này)
    List<TrainingVideo> findByTitleContainingIgnoreCase(String title);
    
    // Tìm kiếm video theo danh mục
    List<TrainingVideo> findByCategory(String category);
}