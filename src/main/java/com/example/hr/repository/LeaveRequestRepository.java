package com.example.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hr.enums.LeaveStatus;
import com.example.hr.models.LeaveRequest;
import com.example.hr.models.User;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByStatus(LeaveStatus pending);

    List<LeaveRequest> findByUser(User user);
@Query("SELECT l FROM LeaveRequest l LEFT JOIN FETCH l.user WHERE (:keyword IS NULL OR l.user.fullName LIKE %:keyword%)")
List<LeaveRequest> findAllWithUser(@Param("keyword") String keyword);

   long countByStatus(LeaveStatus status);

   @EntityGraph(attributePaths = "user")
   List<LeaveRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
   
   // Advanced Leave Management methods — dùng @Query để tránh ambiguous với countByStatus(LeaveStatus)
   @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.status = :statusStr")
   long countByStatusString(@Param("statusStr") String statusStr);

   long countByStartDateLessThanEqualAndEndDateGreaterThanEqualAndStatus(
       java.time.LocalDate endDate, java.time.LocalDate startDate, String status);
   List<LeaveRequest> findByUserAndStatusAndStartDateBetween(
       User user, String status, java.time.LocalDate startDate, java.time.LocalDate endDate);
   List<LeaveRequest> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
       String status, java.time.LocalDate endDate, java.time.LocalDate startDate);
   long countByUserAndStatus(User user, String status);
   List<LeaveRequest> findTop5ByOrderByCreatedAtDesc();
   
   // Team Analytics methods
   @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.user.department = :department AND l.status = 'PENDING'")
   long countPendingByDepartment(@Param("department") com.example.hr.models.Department department);
   
   @Query("SELECT l FROM LeaveRequest l WHERE l.user.department = :department AND l.startDate >= :startDate AND l.endDate <= :endDate")
   List<LeaveRequest> findByDepartmentAndDateRange(@Param("department") com.example.hr.models.Department department, 
                                                     @Param("startDate") java.time.LocalDate startDate, 
                                                     @Param("endDate") java.time.LocalDate endDate);
}
