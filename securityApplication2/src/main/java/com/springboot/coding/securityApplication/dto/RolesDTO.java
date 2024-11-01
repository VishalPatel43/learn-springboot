package com.springboot.coding.securityApplication.dto;

import com.springboot.coding.securityApplication.annotations.UserRoleValidation;
import com.springboot.coding.securityApplication.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RolesDTO {

    @UserRoleValidation
    private Set<Role> roles;

}
