package com.example.hr.repository;

import com.example.hr.enums.AssetStatus;
import com.example.hr.models.CompanyAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyAssetRepository extends JpaRepository<CompanyAsset, Integer>,
        JpaSpecificationExecutor<CompanyAsset> {

    Optional<CompanyAsset> findByAssetCode(String assetCode);

    List<CompanyAsset> findByStatus(AssetStatus status);

    List<CompanyAsset> findByCategory(String category);

    List<CompanyAsset> findByCategoryAndStatus(String category, AssetStatus status);

    @Query("SELECT a FROM CompanyAsset a WHERE a.status = 'AVAILABLE' ORDER BY a.category, a.assetName")
    List<CompanyAsset> findAvailableAssets();

    @Query("SELECT a FROM CompanyAsset a WHERE LOWER(a.assetName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.assetCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CompanyAsset> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT a FROM CompanyAsset a WHERE a.warrantyExpiry IS NOT NULL " +
           "AND a.warrantyExpiry BETWEEN :start AND :end")
    List<CompanyAsset> findWarrantyExpiringSoon(@Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);

    @Query("SELECT a.category, COUNT(a) FROM CompanyAsset a GROUP BY a.category")
    List<Object[]> countByCategory();

    @Query("SELECT a.status, COUNT(a) FROM CompanyAsset a GROUP BY a.status")
    List<Object[]> countByStatus();

    @Query("SELECT COALESCE(SUM(a.currentValue), 0) FROM CompanyAsset a " +
           "WHERE a.status != 'RETIRED'")
    BigDecimal sumTotalAssetValue();

    @Query("SELECT a.category, COALESCE(SUM(a.currentValue), 0) FROM CompanyAsset a " +
           "WHERE a.status != 'RETIRED' GROUP BY a.category")
    List<Object[]> sumValueByCategory();

    long countByStatus(AssetStatus status);

    boolean existsByAssetCode(String assetCode);
}
