package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.dto.LoginDTO;
import com.springboot.coding.securityApplication.dto.LoginResponseDTO;
import com.springboot.coding.securityApplication.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        User user = authenticateUser(loginDTO);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.generateNewSession(user, refreshToken);

        return new LoginResponseDTO(
                user.getUserId(),
                accessToken,
                refreshToken
        );
    }

    @Transactional
    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);

        User user = userService.getUserById(userId);
        String accessToken = jwtService.generateAccessToken(user);
/*      Remove session and generate new session
        String newRefreshToken = jwtService.generateRefreshToken(user);
        sessionService.removeSession(refreshToken);
        sessionService.generateNewSession(user, newRefreshToken);
*/
        return new LoginResponseDTO(
                user.getUserId(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public void logout(String refreshToken) {
        sessionService.removeSession(refreshToken);
    }

    @Override
    public void logoutFromAllDevices() {
        User user = userService.getCurrentUser();
        sessionService.logoutFromAllDevices(user);
    }

    private User authenticateUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        return (User) authentication.getPrincipal();
    }
}
