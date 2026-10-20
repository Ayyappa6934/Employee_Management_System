package com.ayyappa.employee_management.config;

import com.ayyappa.employee_management.entity.AppUser;
import com.ayyappa.employee_management.entity.Department;
import com.ayyappa.employee_management.entity.Role;
import com.ayyappa.employee_management.repository.DepartmentRepository;
import com.ayyappa.employee_management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            DepartmentRepository departments,
            UserRepository users,
            PasswordEncoder encoder,
            @Value("${app.demo.admin-password:Admin@12345}") String adminPassword,
            @Value("${app.demo.employee-password:Employee@12345}") String employeePassword) {

        return args -> {
            if (departments.findByName("IT").isEmpty()) {
                departments.save(new Department("IT"));
            }

            if (departments.findByName("Non-IT").isEmpty()) {
                departments.save(new Department("Non-IT"));
            }

            if (users.findByUsername("admin").isEmpty()) {
                users.save(new AppUser(
                        "admin", encoder.encode(adminPassword), Role.ADMIN));
            }

            if (users.findByUsername("employee").isEmpty()) {
                users.save(new AppUser(
                        "employee",
                        encoder.encode(employeePassword),
                        Role.EMPLOYEE));
            }
        };
    }
}