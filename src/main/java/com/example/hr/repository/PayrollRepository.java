package com.example.hr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hr.models.Payroll;
import com.example.hr.models.User;

public interface PayrollRepository extends JpaRepository<Payroll, Integer> {

    Optional<Payroll> findByUserIdAndMonthAndYear(Integer userId, Integer month, Integer year);

    List<Payroll> findByUser(User user);

    @Query("SELECT p FROM Payroll p LEFT JOIN FETCH p.user WHERE (:keyword IS NULL OR p.user.fullName LIKE %:keyword%)")
    List<Payroll> findAllWithUser(@Param("keyword") String keyword);
}