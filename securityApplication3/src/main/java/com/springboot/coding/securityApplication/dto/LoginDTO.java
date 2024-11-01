package com.springboot.coding.securityApplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "Email of the employee cannot be blank")
    @Email(message = "Email should be a valid email")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password should have at least 4 characters")
    private String password;

}
