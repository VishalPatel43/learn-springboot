package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.auth.CustomUserDetails;
import com.springboot.coding.securityApplication.dto.LoginDTO;
import com.springboot.coding.securityApplication.dto.LoginResponseDTO;
import com.springboot.coding.securityApplication.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );
//        User user1 = (User) authentication.getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);

        // Retrieve the User entity by userId and create a new session
        Long userId = customUserDetails.getUserId();

        User user = userService.getUserById(userId);
        sessionService.generateNewSession(user, refreshToken);

        return new LoginResponseDTO(userId,
//                customUserDetails.getUsername(),
                accessToken,
                refreshToken
        );
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);

        sessionService.validateSession(refreshToken);

        User user = userService.getUserById(userId);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
/*
        * Refresh token contain only Subject not Claims so it will return null for claims
        * Here we set subject as ID, so we can't use email
        String username = jwtService.extractUsername(refreshToken); // email -> null
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
*/

        String accessToken = jwtService.generateAccessToken(customUserDetails);
        return new LoginResponseDTO(
                customUserDetails.getUserId(),
                accessToken,
                refreshToken
        );
    }
}
