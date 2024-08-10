package com.springboot.springbootwebtutorial.controllers;

import com.springboot.springbootwebtutorial.dto.EmployeeDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
//@RequestMapping("/employees")
@RequestMapping(path = "/test/employees")


// @PathVariable and @RequestParam we pass the data (variable) in the URL
// @RequestBody we pass the entire json data of the EmployeeDTO (object, entity etc.)
public class TestController {

//    @GetMapping("/getSecretMessage")
//    public String getMySuperSecretMessage() {
//        return "Secret message: asdfal@#$DASD";
//    }

    //    @GetMapping("/{employeeId}")
//    employeeId --> mandatory path variable in the URL
    @GetMapping(path = "/{employeeId}")
//    public EmployeeDTO getEmployeeById(@PathVariable Long employeeId) {
    public EmployeeDTO getEmployeeById(@PathVariable(name = "employeeId") Long id) {
        return new EmployeeDTO(
//                employeeId,
                id,
                "Vishal Patel",
                "vishal@gmail.com",
                25,
                "",
                0.0,
                LocalDate.of(2021, 1, 1),
                true);
    }

    //    @GetMapping("/getEmployee")
//    http://localhost:8080/employees?age=23&sortBy=asc
    // after ? we use the & for the other fields
    // Optional RequestParam in URL use for the sorting etc
    @GetMapping(path = "")
//    public String getAllEmployees(@RequestParam(required = false) Integer age,
    public String getAllEmployees(@RequestParam(required = false, name = "inputAge") Integer age,
                                  @RequestParam(required = false) String sortBy) { // here we sort by the Age, so we use the RequestParam
        return "Hi age " + age + " sortBy " + sortBy;
    }

    //    @PostMapping(path = "")
//    public String createNewEmployee() {
//        return "Hello from POST method";
//    }
    @PostMapping(path = "")
    public EmployeeDTO createNewEmployee(@RequestBody EmployeeDTO inputEmployee) { // pass entire json data of the EmployeeDTO
        inputEmployee.setId(100L);
        return inputEmployee;
    }

    @PutMapping(path = "")
    public String updateEmployee() {
        return "Hello from PUT method";
    }
}