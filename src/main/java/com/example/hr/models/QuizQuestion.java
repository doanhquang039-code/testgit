package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;
    
    @Column(nullable = false)
    private String type; // MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
    
    @Column(columnDefinition = "TEXT")
    private String options; // JSON array cho multiple choice
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String correctAnswer;
    
    private Integer points = 1;
    
    @Column(nullable = false)
    private Integer orderIndex;
    
    @Column(columnDefinition = "TEXT")
    private String explanation; // Giải thích đáp án
}
