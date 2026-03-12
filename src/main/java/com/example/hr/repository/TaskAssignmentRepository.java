package com.example.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.hr.models.TaskAssignment;
import com.example.hr.models.User;
import java.util.List;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {

    List<TaskAssignment> findByUser(User user);

    @Query("SELECT a FROM TaskAssignment a LEFT JOIN FETCH a.user")
    List<TaskAssignment> findAllWithUser();
}