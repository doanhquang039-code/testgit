package com.example.hr.repository;

import com.example.hr.enums.DocumentType;
import com.example.hr.models.EmployeeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Integer>,
        JpaSpecificationExecutor<EmployeeDocument> {

    List<EmployeeDocument> findByUserId(Integer userId);

    List<EmployeeDocument> findByUserIdAndDocumentType(Integer userId, DocumentType documentType);

    List<EmployeeDocument> findByDocumentType(DocumentType documentType);

    long countByUserId(Integer userId);

    @Query("SELECT d FROM EmployeeDocument d WHERE d.isVerified = false ORDER BY d.uploadedAt DESC")
    List<EmployeeDocument> findUnverifiedDocuments();

    @Query("SELECT d FROM EmployeeDocument d WHERE d.expiryDate IS NOT NULL AND d.expiryDate BETWEEN :start AND :end")
    List<EmployeeDocument> findExpiringSoon(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT d FROM EmployeeDocument d WHERE d.expiryDate IS NOT NULL AND d.expiryDate < :today")
    List<EmployeeDocument> findExpiredDocuments(@Param("today") LocalDate today);

    @Query("SELECT COUNT(d) FROM EmployeeDocument d WHERE d.user.id = :userId AND d.isVerified = true")
    long countVerifiedByUserId(@Param("userId") Integer userId);

    @Query("SELECT d.documentType, COUNT(d) FROM EmployeeDocument d GROUP BY d.documentType")
    List<Object[]> countByDocumentType();

    boolean existsByUserIdAndDocumentType(Integer userId, DocumentType documentType);
}
