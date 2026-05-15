package com.example.hr.repository;

import com.example.hr.models.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Integer> {

    // ✅ Thêm cái này — Controller đang gọi
    List<JobPosition> findByActiveTrue();

    // ✅ Đổi từ findByTitleContainingIgnoreCase → findByPositionNameContainingIgnoreCase
    List<JobPosition> findByPositionNameContainingIgnoreCase(String positionName);

    // ✅ Giữ lại
    List<JobPosition> findByActive(Boolean active);

    @org.springframework.data.jpa.repository.Query("SELECT j FROM JobPosition j WHERE j.active = true " +
           "AND (:keyword IS NULL OR LOWER(j.positionName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:level IS NULL OR j.jobLevel = :level)")
    org.springframework.data.domain.Page<JobPosition> searchPositions(@org.springframework.data.repository.query.Param("keyword") String keyword, 
                                                                      @org.springframework.data.repository.query.Param("level") Integer level, 
                                                                      org.springframework.data.domain.Pageable pageable);
}