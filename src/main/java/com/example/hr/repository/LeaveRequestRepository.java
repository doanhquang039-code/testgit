package com.example.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}

//