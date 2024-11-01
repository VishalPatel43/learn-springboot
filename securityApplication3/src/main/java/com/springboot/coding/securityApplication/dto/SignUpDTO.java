package com.springboot.coding.securityApplication.dto;

import com.springboot.coding.securityApplication.annotations.UserRoleValidation;
import com.springboot.coding.securityApplication.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDTO {

    @NotBlank(message = "Email of the employee cannot be blank")
    @Email(message = "Email should be a valid email")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password should have at least 4 characters")
    private String password;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 10, message = "Number of characters in name should be in the range: [3, 10]")
    private String name;

    @UserRoleValidation
    private Set<Role> roles;

}
