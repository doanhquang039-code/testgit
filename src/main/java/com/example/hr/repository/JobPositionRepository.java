package com.example.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hr.models.JobPosition;


public interface JobPositionRepository extends JpaRepository<JobPosition, Integer> {
	List<JobPosition> findByActiveTrue();
	List<JobPosition> findByTitleContainingIgnoreCase(String title);
}
