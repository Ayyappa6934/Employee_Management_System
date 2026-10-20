package com.ayyappa.employee_management.controller;

import com.ayyappa.employee_management.entity.Employee;

import com.ayyappa.employee_management.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    public record EmployeeRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @PositiveOrZero double salary,
            @NotNull Long departmentId) {}

    @PostMapping("/addEmployee")
    public ResponseEntity<Employee> create(
            @Valid @RequestBody EmployeeRequest request) {
        Employee employee = service.create(
                request.name(), request.email(),
                request.salary(), request.departmentId());

        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @GetMapping
    public List<Employee> getAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public Employee update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        return service.update(
                id, request.name(), request.email(),
                request.salary(), request.departmentId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}