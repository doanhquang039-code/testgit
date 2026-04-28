package com.example.hr.repository;

import com.example.hr.models.AttendanceLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceLocationRepository extends JpaRepository<AttendanceLocation, Integer> {
    
    List<AttendanceLocation> findByIsActiveTrue();
    
    List<AttendanceLocation> findByNameContainingIgnoreCase(String name);
}
