package com.springboot.springbootwebtutorial.controllers;

import com.springboot.springbootwebtutorial.dto.EmployeeDTO;
import com.springboot.springbootwebtutorial.entity.EmployeeEntity;
import com.springboot.springbootwebtutorial.repositories.EmployeeRepository;
import com.springboot.springbootwebtutorial.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// Neither Entity or Repository is defined in the Controller Layer
@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

//    private final EmployeeRepository employeeRepository;
//
//    @Autowired
//    public EmployeeController(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//    @GetMapping(path = "/{employeeId}")
//    public EmployeeEntity getEmployeeById(@PathVariable(name = "employeeId") Long id) {
//        return employeeRepository.findById(id).orElse(null);
//    }
//
//    @GetMapping
//    public List<EmployeeEntity> getAllEmployees() {
//        return employeeRepository.findAll();
//    }
//
//    @PostMapping
//    public EmployeeEntity createNewEmployee(@RequestBody EmployeeEntity inputEmployee) {
//        return employeeRepository.save(inputEmployee);
//    }


    // We remove the Repository from the Controller Layer (Presentation Layer) and move it to the Service Layer (Business Logic Layer)
    // We remove the Entity from the Controller Layer (Presentation Layer) and move it to the Service Layer (Business Logic Layer)


    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/{employeeId}")
    public EmployeeEntity getEmployeeById(@PathVariable(name = "employeeId") Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping
    public List<EmployeeEntity> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public EmployeeEntity createNewEmployee(@RequestBody EmployeeEntity inputEmployee) {
        return employeeService.createNewEmployee(inputEmployee);
    }


}
