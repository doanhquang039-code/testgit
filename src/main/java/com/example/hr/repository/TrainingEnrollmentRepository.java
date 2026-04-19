package com.example.hr.repository;

import com.example.hr.enums.EnrollmentStatus;
import com.example.hr.models.TrainingEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingEnrollmentRepository extends JpaRepository<TrainingEnrollment, Integer> {

    List<TrainingEnrollment> findByUserId(Integer userId);

    List<TrainingEnrollment> findByProgramId(Integer programId);

    List<TrainingEnrollment> findByStatus(EnrollmentStatus status);

    Optional<TrainingEnrollment> findByUserIdAndProgramId(Integer userId, Integer programId);

    boolean existsByUserIdAndProgramId(Integer userId, Integer programId);

    long countByProgramId(Integer programId);

    @Query("SELECT COUNT(e) FROM TrainingEnrollment e WHERE e.program.id = :programId " +
           "AND e.status = 'COMPLETED'")
    long countCompletedByProgramId(@Param("programId") Integer programId);

    @Query("SELECT AVG(e.score) FROM TrainingEnrollment e WHERE e.program.id = :programId " +
           "AND e.score IS NOT NULL")
    Double getAverageScoreByProgram(@Param("programId") Integer programId);

    @Query("SELECT e.status, COUNT(e) FROM TrainingEnrollment e WHERE e.user.id = :userId " +
           "GROUP BY e.status")
    List<Object[]> countByStatusForUser(@Param("userId") Integer userId);

    @Query("SELECT COUNT(e) FROM TrainingEnrollment e WHERE e.user.id = :userId " +
           "AND e.status = 'COMPLETED'")
    long countCompletedByUserId(@Param("userId") Integer userId);

    @Query("SELECT e FROM TrainingEnrollment e WHERE e.user.id = :userId " +
           "AND e.status IN ('ENROLLED', 'IN_PROGRESS') ORDER BY e.enrolledAt DESC")
    List<TrainingEnrollment> findActiveEnrollments(@Param("userId") Integer userId);
}
