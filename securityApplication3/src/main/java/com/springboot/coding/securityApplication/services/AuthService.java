package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.dto.LoginDTO;
import com.springboot.coding.securityApplication.dto.LoginResponseDTO;
import com.springboot.coding.securityApplication.entities.User;

public interface AuthService {

    LoginResponseDTO login(LoginDTO loginDTO);

    LoginResponseDTO refreshToken(String refreshToken);

    void logout(String refreshToken);

    void logoutFromAllDevices();

}
