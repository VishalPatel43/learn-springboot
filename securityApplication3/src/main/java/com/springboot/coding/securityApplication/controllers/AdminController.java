package com.springboot.coding.securityApplication.controllers;

import com.springboot.coding.securityApplication.dto.RolesDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @PutMapping("/users/{userId}/roles")
    public ResponseEntity<User> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody @Valid RolesDTO rolesDTO) {
        User updatedUser = userService.updateRoles(userId, rolesDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
