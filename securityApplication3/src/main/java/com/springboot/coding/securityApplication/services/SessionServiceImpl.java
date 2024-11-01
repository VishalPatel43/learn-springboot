package com.springboot.coding.securityApplication.services;

import com.springboot.coding.securityApplication.entities.Session;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private static final int SESSION_LIMIT = 2; // if subscription based service we can set the dynamic limit

    @Override
    public void generateNewSession(User user, String refreshToken) {
        List<Session> userSessions = sessionRepository.findByUser(user);

        if (userSessions.size() == SESSION_LIMIT) {
            // sort the sessions based on last used time
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));
            Session leastRecentlyUsedSession = userSessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }
/*
        * when we change the limit to dynamic
        * It will ensure that the user has only (SESSION_LIMIT - 1) sessions at a time

        if (userSessions.size() >= SESSION_LIMIT) {
            // Sort sessions by last used time (oldest first) to remove the least recently used ones
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));
            // Remove excess sessions until within SESSION_LIMIT
            for (int i = 0; i <= userSessions.size() - SESSION_LIMIT; i++)
                sessionRepository.delete(userSessions.get(i));
        }
*/

        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }

    @Override
    public void validateSession(String refreshToken) {

        // If there is session for given refreshToken then it is valid token
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: " + refreshToken));

        // whenever we use this session we make it last used at current time
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public void deleteSessionByRefreshToken(String refreshToken) {
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: " + refreshToken));
        sessionRepository.deleteById(session.getSessionId());
    }
}
