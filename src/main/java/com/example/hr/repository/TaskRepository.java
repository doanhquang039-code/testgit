package com.example.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hr.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    // Giữ lại hàm tìm theo tên (Hợp lệ vì Task có field taskName)
    List<Task> findByTaskNameContaining(String keyword);
}