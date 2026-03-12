package com.example.hr.repository;

import java.util.Optional;
import java.util.List; // PHẢI dùng java.util để chứa dữ liệu từ Database

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hr.enums.UserStatus;
import com.example.hr.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    // Bây giờ List<User> sẽ được Spring Data JPA hiểu là danh sách thực thể
    List<User> findByStatus(UserStatus status);

    User findByEmail(String email);

    List<User> findByFullNameContainingAndStatus(String fullName, UserStatus status);

}