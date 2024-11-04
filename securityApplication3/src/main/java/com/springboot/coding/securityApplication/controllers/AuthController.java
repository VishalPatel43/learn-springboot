package com.springboot.coding.securityApplication.controllers;

import com.springboot.coding.securityApplication.dto.*;
import com.springboot.coding.securityApplication.services.AuthService;
import com.springboot.coding.securityApplication.services.SessionService;
import com.springboot.coding.securityApplication.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final SessionService sessionService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody @Valid SignUpDTO signUpDTO) {
        return ResponseEntity.ok(userService.signUp(signUpDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO,
                                                  HttpServletResponse response) {

        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);
        Cookie cookie = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60 * 24 * 7); // 7 days*/
        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDTO);
    }


    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
        // Retrieve the refresh token from cookies
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found in cookies"));

        authService.logout(refreshToken);

        // Clear the refreshToken cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setMaxAge(0); // Set max age to 0 to delete the cookie
        cookie.setPath("/"); // Ensure the correct cookie is removed
        response.addCookie(cookie);

        LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO("Logout successful");
        return ResponseEntity.ok(logoutResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found inside the Cookies"));

        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);

        // Update refresh token cookie
        Cookie cookie = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDTO);
    }


    @PostMapping("/logout-all")
    public ResponseEntity<LogoutResponseDTO> logoutFromAllDevices() {
        authService.logoutFromAllDevices();
        return ResponseEntity.ok(new LogoutResponseDTO("Logout from all devices successful"));
    }
}
