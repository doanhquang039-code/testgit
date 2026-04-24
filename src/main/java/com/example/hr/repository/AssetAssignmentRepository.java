package com.example.hr.repository;

import com.example.hr.models.AssetAssignment;
import com.example.hr.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, Integer> {
    List<AssetAssignment> findByUserOrderByAssignedDateDesc(User user);
    
    @Query("SELECT a FROM AssetAssignment a WHERE a.actualReturn IS NULL")
    List<AssetAssignment> findActiveAssignments();

    boolean existsByAssetIdAndActualReturnIsNull(Integer assetId);

    @Query("SELECT a FROM AssetAssignment a WHERE a.user.id = :userId AND a.actualReturn IS NULL")
    List<AssetAssignment> findCurrentAssignmentsByUser(@Param("userId") Integer userId);

    @Query("SELECT a FROM AssetAssignment a WHERE a.actualReturn IS NULL")
    List<AssetAssignment> findAllActiveAssignments();

    @Query("SELECT a FROM AssetAssignment a WHERE a.actualReturn IS NULL AND a.expectedReturn < :date")
    List<AssetAssignment> findOverdueAssignments(@Param("date") LocalDate date);
}