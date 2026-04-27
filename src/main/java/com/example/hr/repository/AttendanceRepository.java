package com.example.hr.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hr.enums.AttendanceStatus;
import com.example.hr.models.Attendance;
import com.example.hr.models.User;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    List<Attendance> findByUser(User user);

    Optional<Attendance> findByUserAndAttendanceDate(User user, LocalDate date);

    List<Attendance> findByAttendanceDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT a FROM Attendance a JOIN FETCH a.user u " +
           "WHERE (:keyword IS NULL OR u.fullName LIKE %:keyword%) " +
           "ORDER BY a.attendanceDate DESC")
    List<Attendance> findAllWithUser(@Param("keyword") String keyword);

    @Query("SELECT a FROM Attendance a JOIN FETCH a.user u " +
           "WHERE u = :user AND YEAR(a.attendanceDate) = :year AND MONTH(a.attendanceDate) = :month " +
           "ORDER BY a.attendanceDate ASC")
    List<Attendance> findByUserAndYearAndMonth(@Param("user") User user,
                                               @Param("year") int year,
                                               @Param("month") int month);
    
    // Advanced Analytics methods
    long countByAttendanceDateAndStatus(LocalDate date, AttendanceStatus status);
    long countByAttendanceDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, AttendanceStatus status);
    boolean existsByUserAndAttendanceDateAndStatus(User user, LocalDate date, AttendanceStatus status);
    boolean existsByUserAndAttendanceDate(User user, LocalDate date);
    List<Attendance> findByUserAndAttendanceDateBetweenOrderByAttendanceDateDesc(User user, LocalDate startDate, LocalDate endDate);
}
