package com.springboot.springbootwebtutorial.services;

import com.springboot.springbootwebtutorial.dto.EmployeeDTO;
import com.springboot.springbootwebtutorial.entity.EmployeeEntity;
import com.springboot.springbootwebtutorial.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Service Layer (Business Logic Layer)
@Service
public class EmployeeService {

    final EmployeeRepository employeeRepository;
    final ModelMapper modelMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    // Business Logic
    public Optional<EmployeeDTO> getEmployeeById(Long id) {

        // 1
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
//        EmployeeDTO employeeDTO = modelMapper.map(employeeEntity, EmployeeDTO.class);
//        Optional<EmployeeDTO> optionalEmployeeDTO = Optional.of(employeeDTO);
//        return optionalEmployeeDTO;

        // 2
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
//        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1, EmployeeDTO.class));

        // 3
        return employeeRepository
                .findById(id)
                .map(employeeEntity -> modelMapper // Convert to the Optional<EmployeeDTO>
                        .map(employeeEntity, EmployeeDTO.class) // this is the mapping of the Entity to DTO
                );
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        return employeeEntities
                .stream()
                .map(employeeEntity -> modelMapper
                        .map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
//                .toList();
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
        EmployeeEntity toSaveEntity = modelMapper.map(inputEmployee, EmployeeEntity.class);
        EmployeeEntity saveEmployeeEntity = employeeRepository.save(toSaveEntity);
        return modelMapper.map(saveEmployeeEntity, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeById(Long employeeId, EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        // If it's present then it will update the record otherwise it will create new employee
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);
    }

    public boolean isExistsByEmployeeId(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    public boolean deleteEmployeeById(Long employeeId) {
        boolean isExists = isExistsByEmployeeId(employeeId);
        if (!isExists) return false;
        employeeRepository.deleteById(employeeId);
        return true;
    }

    // Using Reflection we can directly go to that object and update the fields of that object
    // directly without using the getter and setter methods.
    // directly with help of Reflection
    // using reflection we inversely update the field value directly
    public EmployeeDTO updatePartialEmployeeById(Long employeeId, Map<String, Object> updates) {
        boolean isExists = isExistsByEmployeeId(employeeId);
        if (!isExists) return null;

        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).orElse(null);
//        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).get();
//        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Employee not found with id: " + employeeId)
//                );
        updates.forEach((field, value) -> {
            Field fieldTobeUpdated = ReflectionUtils.findField(EmployeeEntity.class, field);
            fieldTobeUpdated.setAccessible(true); // to access the private fields in Entity class with Reflection and without getter and setter methods
            ReflectionUtils.setField(fieldTobeUpdated, employeeEntity, value);
        });

        EmployeeEntity updatedEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(updatedEmployeeEntity, EmployeeDTO.class);
    }
}
