package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.entities.Session;
import com.springboot.coding.securityApplication.entities.User;

import java.util.List;

public interface SessionService {

    void generateNewSession(User user, String refreshToken);

    void validateSession(String refreshToken);

    void removeSession(String refreshToken);

    List<Session> getUserSessions(User user);

    void logoutFromAllDevices(User user);
}
