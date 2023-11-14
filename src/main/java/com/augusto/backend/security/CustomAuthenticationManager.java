package com.augusto.backend.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public CustomAuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CredentialsHelper credentialsHelper = (CredentialsHelper) authentication.getCredentials();

        if (jwtUtil.isTokenValid(credentialsHelper.getToken())) {
            String email = jwtUtil.getAllClaimsFromToken(credentialsHelper.getToken()).getSubject();
            return Mono.just(new UsernamePasswordAuthenticationToken(email, credentialsHelper));
        }

        return Mono.empty(); // Authentication failed
    }


}
