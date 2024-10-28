package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.auth.CustomUserDetails;
import com.springboot.coding.securityApplication.dto.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

//        User user = (User) authentication.getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return jwtUtil.generateAccessToken(customUserDetails);
    }

}
