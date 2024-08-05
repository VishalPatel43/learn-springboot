package com.springboot.springbootwebtutorial.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


// In DTO we can add certain validations and constraints that we want to apply on the data that we are receiving from the client.
// We put just the data that we want to send to the client and ignore like password etc.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    Long id;
    String name;
    String email;
    Integer age;
    LocalDate dateOfJoining;
    Boolean isActive;
}