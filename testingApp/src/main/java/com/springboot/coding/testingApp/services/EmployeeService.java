package com.springboot.coding.testingApp.services;

import com.springboot.coding.testingApp.dto.EmployeeDTO;
import com.springboot.coding.testingApp.entities.Employee;

public interface EmployeeService {

    Employee getEmployeeById(Long employeeId);

//    Employee getEmployeeByEmail(String email);

    EmployeeDTO getEmployeeDTOById(Long employeeId);

//    EmployeeDTO getEmployeeDTOByEmail(String email);

    EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO);

    void deleteEmployee(Long employeeId);

}
