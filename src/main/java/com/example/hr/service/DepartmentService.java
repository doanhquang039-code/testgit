package com.example.hr.service;

import com.example.hr.models.Department;
import com.example.hr.repository.DepartmentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "departments", key = "'all'")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id: " + id));
    }

    @CacheEvict(value = "departments", allEntries = true)
    public Department saveDepartment(Department dept) {
        return departmentRepository.save(dept);
    }

    @CacheEvict(value = "departments", allEntries = true)
    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }
}
