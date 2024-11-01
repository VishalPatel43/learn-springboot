package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.auth.CustomUserDetails;
import com.springboot.coding.securityApplication.dto.RolesDTO;
import com.springboot.coding.securityApplication.dto.SignUpDTO;
import com.springboot.coding.securityApplication.dto.UpdatePasswordDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.entities.enums.Role;
import com.springboot.coding.securityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

        toBeCreatedUser.setRoles(toBeCreatedUser.getRoles() == null ? Set.of(Role.USER) : toBeCreatedUser.getRoles());

        User savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDTO.class);

    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
/* Get the current user from the authentication object

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
*/

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails customUserDetails))
            throw new UsernameNotFoundException("No authenticated user found");

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + authentication.getName()));
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
        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public User updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {
        User user = getUserById(userId);
        User currentUser = getCurrentUser();

        if (!currentUser.getUserId().equals(user.getUserId()))
            throw new AccessDeniedException("You are not authorized to update password for another user");

        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPassword()))
            throw new BadCredentialsException("Current password is incorrect");

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        return userRepository.save(user);  // Returns the updated user
    }

    @Transactional
    @Override
    public User updateUserProfile(Long userId, UserDTO userDTO) {
        User user = getUserById(userId);
        User currentUser = getCurrentUser();

        if (!currentUser.getUserId().equals(user.getUserId()))
            throw new AccessDeniedException("You are not authorized to update password for another user");

        // Update user fields from the UserDTO (example fields, customize as needed)
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        return userRepository.save(user);  // Returns the updated user
    }

    @Transactional
    public User updateRoles(Long userId, RolesDTO rolesDTO) {
        User currentUser = getCurrentUser();

        // Check if the current user is an admin
        if (!currentUser.getRoles().contains(Role.ADMIN))
            throw new AccessDeniedException("Only admins can update user roles");

        // Fetch the user to be updated
        User userToUpdate = getUserById(userId);

        // Update the roles
        userToUpdate.setRoles(rolesDTO.getRoles());
        return userRepository.save(userToUpdate); // Returns the updated user with new roles
    }
}
