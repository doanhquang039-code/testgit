package com.example.hr.repository;

import com.example.hr.models.AssetAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, Integer> {

    List<AssetAssignment> findByUserId(Integer userId);

    List<AssetAssignment> findByAssetId(Integer assetId);

    @Query("SELECT aa FROM AssetAssignment aa WHERE aa.asset.id = :assetId " +
           "AND aa.actualReturn IS NULL")
    Optional<AssetAssignment> findCurrentAssignmentByAsset(@Param("assetId") Integer assetId);

    @Query("SELECT aa FROM AssetAssignment aa WHERE aa.user.id = :userId " +
           "AND aa.actualReturn IS NULL ORDER BY aa.assignedDate DESC")
    List<AssetAssignment> findCurrentAssignmentsByUser(@Param("userId") Integer userId);

    @Query("SELECT aa FROM AssetAssignment aa WHERE aa.actualReturn IS NULL " +
           "AND aa.expectedReturn IS NOT NULL AND aa.expectedReturn < :today")
    List<AssetAssignment> findOverdueAssignments(@Param("today") LocalDate today);

    @Query("SELECT aa FROM AssetAssignment aa WHERE aa.actualReturn IS NULL " +
           "ORDER BY aa.assignedDate DESC")
    List<AssetAssignment> findAllActiveAssignments();

    @Query("SELECT aa.user.department.departmentName, COUNT(aa) FROM AssetAssignment aa " +
           "WHERE aa.actualReturn IS NULL GROUP BY aa.user.department.departmentName")
    List<Object[]> countActiveByDepartment();

    @Query("SELECT COUNT(aa) FROM AssetAssignment aa WHERE aa.actualReturn IS NULL")
    long countActiveAssignments();

    @Query("SELECT COUNT(aa) FROM AssetAssignment aa WHERE aa.actualReturn IS NULL " +
           "AND aa.expectedReturn IS NOT NULL AND aa.expectedReturn < :today")
    long countOverdueAssignments(@Param("today") LocalDate today);

    boolean existsByAssetIdAndActualReturnIsNull(Integer assetId);
}
