package com.springboot.coding.securityApplication.utils;


import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.entities.enums.SubscriptionPlan;
import com.springboot.coding.securityApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.springboot.coding.securityApplication.entities.enums.Role.ADMIN;

@Component
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserService userService;

    public boolean hasAccessBasedOnPlan(String requiredPlan) {
        User user = userService.getCurrentUser();
        // Allow access if the user is an admin
        if (user.getRoles().contains(ADMIN))
            return true;

        SubscriptionPlan userPlan = user.getSubscriptionPlan();
        return userPlan.ordinal() >= SubscriptionPlan.valueOf(requiredPlan).ordinal();
    }
}

