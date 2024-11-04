package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.dto.RolesDTO;
import com.springboot.coding.securityApplication.dto.SignUpDTO;
import com.springboot.coding.securityApplication.dto.UpdatePasswordDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.Session;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.entities.enums.Role;
import com.springboot.coding.securityApplication.entities.enums.SubscriptionPlan;
import com.springboot.coding.securityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.springboot.coding.securityApplication.entities.enums.Role.*;
import static com.springboot.coding.securityApplication.entities.enums.SubscriptionPlan.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    @Transactional
    @Override
    public UserDTO signUp(SignUpDTO signUpDTO) {

        userRepository.findByEmail(signUpDTO.getEmail())
                .ifPresent(userExist -> {
                    throw new BadCredentialsException("User with email already exits " + signUpDTO.getEmail());
                });

        User newUser = modelMapper.map(signUpDTO, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        newUser.setRoles(resolveRoles(newUser.getRoles()));

        // Set default subscription plan if not provided
        SubscriptionPlan subscriptionPlan = signUpDTO.getSubscriptionPlan();
        if (newUser.getRoles().contains(ADMIN))
            newUser.setSubscriptionPlan(ADMIN_ACCESS);
        else
            newUser.setSubscriptionPlan(
                    (subscriptionPlan == null || subscriptionPlan.toString().trim().isEmpty()) ?
                            FREE : subscriptionPlan
            );

        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDTO.class);

    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user))
            throw new UsernameNotFoundException("No authenticated user found");
        return user;
    }

    @Override
    public UserDTO getUserProfile() {
        User currentUser = getCurrentUser();
        return modelMapper.map(currentUser, UserDTO.class);
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

    @Transactional
    @Override
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public User updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO) {
        User user = validateUserAccess(userId);
        validateCurrentPassword(user, updatePasswordDTO.getCurrentPassword());

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        return userRepository.save(user);  // Returns the updated user
    }

    @Transactional
    @Override
    public User updateUserProfile(Long userId, UserDTO userDTO) {
        User user = validateUserAccess(userId);

        // Update user fields from the UserDTO (example fields, customize as needed)
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        return userRepository.save(user);  // Returns the updated user
    }

    @Transactional
    @Override
    public User updateRoles(Long userId, RolesDTO rolesDTO) {
        User currentUser = getCurrentUser();

        // Check if the current user is an admin
        if (!currentUser.getRoles().contains(ADMIN))
            throw new AccessDeniedException("Only admins can update user roles");

        // Fetch the user to be updated
        User userToUpdate = getUserById(userId);

        // Update the roles
        userToUpdate.setRoles(rolesDTO.getRoles());
        return userRepository.save(userToUpdate); // Returns the updated user with new roles
    }

    @Override
    public int getCurrentUserSessionCount() {
        User currentUser = getCurrentUser();
        List<Session> sessions = sessionService.getUserSessions(currentUser);
        return sessions.size();
    }

    private Set<Role> resolveRoles(Set<Role> roles) {

        log.info("Provided roles: {}", roles);

        if (roles == null || roles.isEmpty())
            return Set.of(USER); // Default to USER if no roles are provided

        if (roles.contains(ADMIN))
            return Set.of(USER, CREATOR, ADMIN); // If ADMIN is provided, assign all roles

        return roles.contains(CREATOR)
                ? Set.of(USER, CREATOR) // If CREATOR is provided, assign USER and CREATOR roles
                : Set.copyOf(roles); // Otherwise, return the provided roles as-is --> if we provide the user
    }

    private User validateUserAccess(Long userId) {
        User user = getUserById(userId);
        User currentUser = getCurrentUser();

        if (!currentUser.getUserId().equals(user.getUserId()))
            throw new AccessDeniedException("Unauthorized access");
        return user;
    }

    private void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword()))
            throw new BadCredentialsException("Incorrect current password");
    }
}
