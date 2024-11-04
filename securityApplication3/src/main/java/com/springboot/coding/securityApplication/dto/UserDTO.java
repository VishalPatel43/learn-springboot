package com.springboot.coding.securityApplication.dto;

import com.springboot.coding.securityApplication.entities.enums.Role;
import com.springboot.coding.securityApplication.entities.enums.SubscriptionPlan;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long userId;

    @NotBlank(message = "Email of the employee cannot be blank")
    @Email(message = "Email should be a valid email")
    private String email;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 10, message = "Number of characters in name should be in the range: [3, 10]")
    private String name;

    //    @UserRoleValidation --> we can't use it coz we need this dto for update the user and roles update by only ADMIN
    private Set<Role> roles;

    private SubscriptionPlan subscriptionPlan;

}
