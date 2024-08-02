package com.springboot.springbootwebtutorial.dto;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    Long id;
    String name;
    String email;
    Integer age;
    LocalDate dateOfJoining;

    Boolean active;
}
