package com.springboot.coding.securityApplication.controllers;

import com.springboot.coding.securityApplication.dto.UpdatePasswordDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
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
}
