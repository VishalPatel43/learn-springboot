package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.configs.CustomUserDetails;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    // This method come from UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found with email: " + email));

        return new CustomUserDetails(user);
    }
}
