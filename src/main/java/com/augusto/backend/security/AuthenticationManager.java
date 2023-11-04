package com.augusto.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveUserDetailsService userService;

    @Autowired
    public AuthenticationManager(ReactiveUserDetailsService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return userService.findByUsername(authentication.getName())
                .filter(UserDetails::isEnabled)
                .map(user -> authentication);
    }
}
