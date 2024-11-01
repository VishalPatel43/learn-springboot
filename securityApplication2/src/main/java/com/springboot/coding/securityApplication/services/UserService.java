package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.dto.RolesDTO;
import com.springboot.coding.securityApplication.dto.SignUpDTO;
import com.springboot.coding.securityApplication.dto.UpdatePasswordDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;


public interface UserService {

    UserDTO signUp(SignUpDTO signUpDTO);

    User getCurrentUser();

    User getUserById(Long userId);

    User getUserByEmail(String email);

    User save(User newUser);

    User updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO);

    User updateUserProfile(Long userId, UserDTO userDTO);

    User updateRoles(Long userId, RolesDTO rolesDTO);
}
