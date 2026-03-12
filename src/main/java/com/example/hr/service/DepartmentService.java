package com.example.hr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hr.models.Department;
import com.example.hr.repository.DepartmentRepository;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void saveDepartment(Department dept) {
        departmentRepository.save(dept);
    }
}