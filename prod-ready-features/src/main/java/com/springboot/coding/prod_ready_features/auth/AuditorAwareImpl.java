package com.springboot.coding.prod_ready_features.auth;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // get security context
        // get authentication
        // get the principle
        // get username
        return Optional.of("Vishal Patel");
    }
}
