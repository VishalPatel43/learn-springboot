package com.springboot.coding.testingApp.services.impl;

import com.springboot.coding.testingApp.dto.EmployeeDTO;
import com.springboot.coding.testingApp.entities.Employee;
import com.springboot.coding.testingApp.exceptions.ResourceNotFoundException;
import com.springboot.coding.testingApp.repositories.EmployeeRepository;
import com.springboot.coding.testingApp.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public Employee getEmployeeById(Long employeeId) {
        log.info("Fetching employee with ID: {}", employeeId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", employeeId);
                    return new ResourceNotFoundException("Employee not found with ID: " + employeeId);
                });
    }

    /* Not needed

        @Override
        public Employee getEmployeeByEmail(String email) {
            log.info("Fetching employee with email: {}", email);

            return employeeRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.error("Employee not found with email: {}", email);
                        return new ResourceNotFoundException("Employee not found with email: " + email);
                    });
        }

    */
    @Override
    public EmployeeDTO getEmployeeDTOById(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        log.info("Successfully fetched employee with employeeId: {}", employeeId);
        return convertToDTO(employee);
    }
/* Not needed
    @Override
    public EmployeeDTO getEmployeeDTOByEmail(String email) {
        Employee employee = getEmployeeByEmail(email);
        log.info("Successfully fetched employee with email: {}", email);
        return convertToDTO(employee);
    }
*/

    @Override
    public EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating new employee with email: {}", employeeDTO.getEmail());

        employeeRepository.findByEmail(employeeDTO.getEmail())
                .ifPresent(existingEmployee -> {
                    log.error("Employee already exists with email: {}", employeeDTO.getEmail());
                    throw new RuntimeException("Employee already exists with email: " + employeeDTO.getEmail());
                });

        Employee newEmployee = modelMapper.map(employeeDTO, Employee.class);
        Employee savedEmployee = employeeRepository.save(newEmployee);
        log.info("Successfully created new employee with employeeId: {}", savedEmployee.getEmployeeId());
        return convertToDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
        log.info("Updating employee with employeeId: {}", employeeId);
        Employee employee = getEmployeeById(employeeId);

        if (!employee.getEmail().equals(employeeDTO.getEmail())) {
            log.error("Attempted to update email for employee with employeeId: {}", employeeId);
            throw new RuntimeException("The email of the employee cannot be updated");
        }

        modelMapper.map(employeeDTO, employee);
//        employee.setEmployeeId(employeeId);

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully updated employee with employeeId: {}", employeeId);
        return convertToDTO(savedEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.info("Deleting employee with employeeId: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            log.error("Employee not found with employeeId: {}", employeeId);
            throw new ResourceNotFoundException("Employee not found with employeeId: " + employeeId);
        }

        employeeRepository.deleteById(employeeId);
        log.info("Successfully deleted employee with employeeId: {}", employeeId);
    }

    // Private helper method to convert Employee to EmployeeDTO
    private EmployeeDTO convertToDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }
}
