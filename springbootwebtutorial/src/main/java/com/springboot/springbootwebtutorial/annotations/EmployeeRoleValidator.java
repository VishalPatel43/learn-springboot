package com.springboot.springbootwebtutorial.annotations;

// Here we provide the custom validation (checking) logic for the annotation

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

// First: Name of the Annotation
// Second: Type of the Field to be checked (Validate)
public class EmployeeRoleValidator implements ConstraintValidator<EmployeeRoleValidation, String> {


    @Override
    public boolean isValid(String inputRole, ConstraintValidatorContext constraintValidatorContext) {
        // inputRole: user passing this string
        List<String> roles = List.of("USER", "ADMIN"); // Here we can add easily instead of changing the @Pattern in DTO
        return roles.contains(inputRole);
    }
}
