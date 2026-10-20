package com.ayyappa.employee_management.service;

import com.ayyappa.employee_management.entity.Department;
import com.ayyappa.employee_management.entity.Employee;
import com.ayyappa.employee_management.repository.DepartmentRepository;
import com.ayyappa.employee_management.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employees;
    private final DepartmentRepository departments;

    public EmployeeService(
            EmployeeRepository employees,
            DepartmentRepository departments) {
        this.employees = employees;
        this.departments = departments;
    }

    public Employee create(String name, String email,
                           double salary, Long departmentId) {

        Department department = departments.findById(departmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Department not found"));

        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setSalary(salary);
        employee.setDepartment(department);

        return employees.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employees.findAll();
    }

    public Employee update(Long id, String name, String email,
                           double salary, Long departmentId) {

        Employee employee = employees.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found"));

        Department department = departments.findById(departmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Department not found"));

        employee.setName(name);
        employee.setEmail(email);
        employee.setSalary(salary);
        employee.setDepartment(department);

        return employees.save(employee);
    }

    public void delete(Long id) {
        Employee employee = employees.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found"));

        employees.delete(employee);
    }
}