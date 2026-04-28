package com.example.hr.repository;

import com.example.hr.models.Course;
import com.example.hr.models.CourseLesson;
import com.example.hr.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    
    List<Quiz> findByCourse(Course course);
    
    List<Quiz> findByLesson(CourseLesson lesson);
    
    List<Quiz> findByIsActiveTrue();
}
