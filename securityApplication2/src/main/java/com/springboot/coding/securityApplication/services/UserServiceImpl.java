package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.auth.CustomUserDetails;
import com.springboot.coding.securityApplication.dto.SignUpDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO signUp(SignUpDTO signUpDTO) {

        userRepository.findByEmail(signUpDTO.getEmail())
                .ifPresent(userExist -> {
                    throw new BadCredentialsException("User with email already exits " + signUpDTO.getEmail());
                });

        User toBeCreatedUser = modelMapper.map(signUpDTO, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        User savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDTO.class);

    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {

            String username = authentication.getName(); // email
//            username = customUserDetails.getUsername(); // email
            return userRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
//            return userRepository
//                    .findById(customUserDetails.getUserId())  // Use userId to find the user
//                    .orElseThrow(() -> new UsernameNotFoundException("User with ID " + customUserDetails.getUserId() + " not found"));
        }

        // If the authentication or CustomUserDetails is not available, return null or throw an exception
        throw new UsernameNotFoundException("No authenticated user found");

    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID " + userId + " not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
//                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public User save(User newUser) {
//            String password = newUser.getPassword();
//            if (password != null && !password.isBlank()) {
        //            password = passwordEncoder.encode("some default password for OAuth2 users");
        //            newUser.setPassword(password);
        return userRepository.save(newUser);
    }
}
