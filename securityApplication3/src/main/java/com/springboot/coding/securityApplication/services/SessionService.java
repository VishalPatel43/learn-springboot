package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.entities.User;

public interface SessionService {

    void generateNewSession(User user, String refreshToken);

    void validateSession(String refreshToken);

    void deleteSessionByRefreshToken(String refreshToken);
}
