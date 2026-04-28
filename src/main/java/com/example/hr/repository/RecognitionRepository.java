package com.example.hr.repository;

import com.example.hr.models.Recognition;
import com.example.hr.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecognitionRepository extends JpaRepository<Recognition, Integer> {
    
    List<Recognition> findByRecipient(User recipient);
    
    List<Recognition> findByGiver(User giver);
    
    List<Recognition> findByType(String type);
    
    List<Recognition> findByIsPublicTrueOrderByCreatedAtDesc();
    
    @Query("SELECT SUM(r.points) FROM Recognition r WHERE r.recipient = :user")
    Integer getTotalPointsByRecipient(@Param("user") User user);
    
    @Query("SELECT r FROM Recognition r WHERE r.isPublic = true ORDER BY r.createdAt DESC")
    List<Recognition> findRecentPublicRecognitions();
    
    long countByRecipient(User recipient);
}
