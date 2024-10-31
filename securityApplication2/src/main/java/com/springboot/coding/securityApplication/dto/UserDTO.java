package com.springboot.coding.securityApplication.dto;

import com.springboot.coding.securityApplication.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long userId;
    private String email;
    private String name;
    private Set<Role> roles;

}
