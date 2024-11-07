package com.springboot.coding.testingApp.controllers;

import com.springboot.coding.testingApp.dto.EmployeeDTO;
import com.springboot.coding.testingApp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeDTOById(employeeId);
        return ResponseEntity.ok(employeeDTO);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createNewEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployeeDTO = employeeService.createNewEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployeeDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeId, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee with employeeId: " + employeeId + " has been deleted");
    }
}
