package com.example.hr.repository;

import com.example.hr.models.JobPosting;
import com.example.hr.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {

    List<JobPosting> findByStatusOrderByCreatedAtDesc(String status);

    List<JobPosting> findByDepartmentOrderByCreatedAtDesc(Department department);

    List<JobPosting> findByPostedByOrderByCreatedAtDesc(Integer postedBy);

    @Query("SELECT j FROM JobPosting j WHERE j.status = 'ACTIVE' AND (j.closingDate IS NULL OR j.closingDate > :currentDate)")
    List<JobPosting> findActiveJobPostings(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT j FROM JobPosting j WHERE j.title LIKE %:keyword% OR j.description LIKE %:keyword%")
    List<JobPosting> searchByKeyword(@Param("keyword") String keyword);

    List<JobPosting> findByEmploymentTypeAndStatusOrderByCreatedAtDesc(String employmentType, String status);

    List<JobPosting> findByExperienceLevelAndStatusOrderByCreatedAtDesc(String experienceLevel, String status);

    @Query("SELECT COUNT(j) FROM JobPosting j WHERE j.status = :status")
    long countByStatus(@Param("status") String status);

    @Query("SELECT j FROM JobPosting j WHERE j.closingDate BETWEEN :startDate AND :endDate")
    List<JobPosting> findJobsClosingSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(j.applicationsCount) FROM JobPosting j WHERE j.status = 'ACTIVE'")
    Long getTotalApplicationsCount();

    @Query("SELECT AVG(j.applicationsCount) FROM JobPosting j WHERE j.status = 'ACTIVE'")
    Double getAverageApplicationsPerJob();

    // Additional methods for compatibility
    List<JobPosting> findByTitleContainingIgnoreCase(String title);
    
    List<JobPosting> findByStatus(String status);
}