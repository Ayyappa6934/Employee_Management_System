package com.ayyappa.employee_management.repository;

import com.ayyappa.employee_management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);
}