package com.springboot.coding.securityApplication.controllers;

import com.springboot.coding.securityApplication.dto.UpdatePasswordDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
//@Secured("ROLE_USER") --> we already provide the .authenticated() in the WebSecurityConfig
public class UserController {

    private final UserServiceImpl userService;

    @PutMapping("/updatePassword/{userId}")
    public ResponseEntity<User> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User updatedUser = userService.updatePassword(userId, updatePasswordDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUserProfile(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

//    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/session-count")
    public ResponseEntity<Integer> getCurrentUserSessionCount() {
        int sessionCount = userService.getCurrentUserSessionCount();
        return ResponseEntity.ok(sessionCount);
    }
    // Delete the User if his is the owner of the account (use the PreAuthorize annotation)
}
