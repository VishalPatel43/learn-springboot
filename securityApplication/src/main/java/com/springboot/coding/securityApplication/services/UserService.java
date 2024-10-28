package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.dto.SignUpDTO;
import com.springboot.coding.securityApplication.dto.UserDTO;
import com.springboot.coding.securityApplication.entities.User;

public interface UserService {

    UserDTO signUp(SignUpDTO signUpDTO);

    User getCurrentUser();

    User findUserByEmail(String username);

}
