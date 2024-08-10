package com.springboot.springbootwebtutorial.controllers;

import com.springboot.springbootwebtutorial.dto.EmployeeDTO;
import com.springboot.springbootwebtutorial.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


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


    // We can Show the response message in the Controller Layer (Presentation Layer)
    // Here We use the ResponseEntity to show the response message and Status code

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/{employeeId}")
//    public ResponseEntity<Object> getEmployeeById(@PathVariable(name = "employeeId") Long id) {
//    public ResponseEntity<?> getEmployeeById(@PathVariable(name = "employeeId") Long id) {
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable(name = "employeeId") Long id) {
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(id);

//        return employeeDTO.map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1)).orElse(ResponseEntity.notFound().build());
//        return employeeDTO.map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1)).orElseGet(() -> ResponseEntity.notFound().build());
//        return employeeDTO
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build()
//                );
        return employeeDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()
                );
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // @Valid is used to validate the input data which is coming from the client side
    // WE define this valid in the DTO class
    @PostMapping
    public ResponseEntity<EmployeeDTO> createNewEmployee(@RequestBody @Valid EmployeeDTO inputEmployee) {
        EmployeeDTO employeeDTO = employeeService.createNewEmployee(inputEmployee);
        return new ResponseEntity<>(employeeDTO, HttpStatus.CREATED); // we can use different types of status codes we use this method
    }

    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@RequestBody @Valid EmployeeDTO employeeDTO,
                                                          @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.updateEmployeeById(employeeId, employeeDTO));
    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable Long employeeId) {
        boolean gotDeleted = employeeService.deleteEmployeeById(employeeId);
        if (gotDeleted) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updatePartialEmployeeById(@RequestBody Map<String, Object> updates,
                                                                 @PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.updatePartialEmployeeById(employeeId, updates);
        if (employeeDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeDTO);
    }
}
