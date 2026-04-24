package com.example.hr.repository;

import com.example.hr.enums.OvertimeStatus;
import com.example.hr.models.OvertimeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequest, Integer>,
        JpaSpecificationExecutor<OvertimeRequest> {

    List<OvertimeRequest> findByUserId(Integer userId);

    List<OvertimeRequest> findByStatus(OvertimeStatus status);

    List<OvertimeRequest> findByUserIdAndStatus(Integer userId, OvertimeStatus status);

    @Query("SELECT o FROM OvertimeRequest o WHERE o.user.id = :userId " +
           "AND MONTH(o.overtimeDate) = :month AND YEAR(o.overtimeDate) = :year")
    List<OvertimeRequest> findByUserAndMonth(@Param("userId") Integer userId,
                                             @Param("month") int month,
                                             @Param("year") int year);

    @Query("SELECT o FROM OvertimeRequest o WHERE o.status = 'PENDING' ORDER BY o.createdAt ASC")
    List<OvertimeRequest> findPendingRequests();

    @Query("SELECT COALESCE(SUM(o.totalHours), 0) FROM OvertimeRequest o " +
           "WHERE o.user.id = :userId AND o.status = 'APPROVED' " +
           "AND MONTH(o.overtimeDate) = :month AND YEAR(o.overtimeDate) = :year")
    BigDecimal sumApprovedOvertimeHours(@Param("userId") Integer userId,
                                        @Param("month") int month,
                                        @Param("year") int year);

    @Query("SELECT o FROM OvertimeRequest o WHERE o.overtimeDate BETWEEN :start AND :end " +
           "AND o.status = 'APPROVED' ORDER BY o.overtimeDate")
    List<OvertimeRequest> findApprovedBetweenDates(@Param("start") LocalDate start,
                                                    @Param("end") LocalDate end);

    @Query("SELECT o.user.department.departmentName, SUM(o.totalHours) " +
           "FROM OvertimeRequest o WHERE o.status = 'APPROVED' " +
           "AND YEAR(o.overtimeDate) = :year GROUP BY o.user.department.departmentName")
    List<Object[]> sumOvertimeByDepartment(@Param("year") int year);

    @Query("SELECT COUNT(o) FROM OvertimeRequest o WHERE o.user.id = :userId " +
           "AND o.overtimeDate = :date")
    long countByUserAndDate(@Param("userId") Integer userId, @Param("date") LocalDate date);

    long countByStatus(OvertimeStatus status);

    @EntityGraph(attributePaths = "user")
    List<OvertimeRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
