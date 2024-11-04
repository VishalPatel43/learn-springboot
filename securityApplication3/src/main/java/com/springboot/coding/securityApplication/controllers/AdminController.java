package com.springboot.coding.securityApplication.controllers;

import com.springboot.coding.securityApplication.dto.RolesDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN") // --> Role based security
public class AdminController {

    private final UserServiceImpl userService;

    @PutMapping("/users/{userId}/roles")
    public ResponseEntity<User> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody @Valid RolesDTO rolesDTO) {
        User updatedUser = userService.updateRoles(userId, rolesDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
