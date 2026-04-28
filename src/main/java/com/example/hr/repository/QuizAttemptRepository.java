package com.example.hr.repository;

import com.example.hr.models.Quiz;
import com.example.hr.models.QuizAttempt;
import com.example.hr.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {
    
    List<QuizAttempt> findByUser(User user);
    
    List<QuizAttempt> findByQuiz(Quiz quiz);
    
    List<QuizAttempt> findByUserAndQuiz(User user, Quiz quiz);
    
    Optional<QuizAttempt> findFirstByUserAndQuizOrderByScoreDesc(User user, Quiz quiz);
    
    @Query("SELECT AVG(a.score) FROM QuizAttempt a WHERE a.quiz = :quiz")
    Double getAverageScore(@Param("quiz") Quiz quiz);
    
    long countByUserAndPassed(User user, boolean passed);
}
