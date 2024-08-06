package com.springboot.springbootwebtutorial.services;

import com.springboot.springbootwebtutorial.dto.EmployeeDTO;
import com.springboot.springbootwebtutorial.entity.EmployeeEntity;
import com.springboot.springbootwebtutorial.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service Layer (Business Logic Layer)
@Service
public class EmployeeService {

    final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Business Logic
    public EmployeeEntity getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }


    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity createNewEmployee(EmployeeEntity inputEmployee) {
        return employeeRepository.save(inputEmployee);
    }
}
