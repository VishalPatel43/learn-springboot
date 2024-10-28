package com.springboot.coding.securityApplication.controllers;

import com.springboot.coding.securityApplication.dto.LoginDTO;
import com.springboot.coding.securityApplication.dto.SignUpDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.services.AuthService;
import com.springboot.coding.securityApplication.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        return ResponseEntity.ok(userService.signUp(signUpDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO,
                                        HttpServletResponse response) {

        String token = authService.login(loginDTO);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
        response.addCookie(cookie);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
