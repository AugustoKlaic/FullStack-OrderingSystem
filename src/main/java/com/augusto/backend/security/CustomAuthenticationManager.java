package com.augusto.backend.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            Claims claims = jwtUtil.getAllClaimsFromToken(credentialsHelper.getToken());
            String email = claims.getSubject();
            Collection<GrantedAuthority> roles = extractRoles(claims);
            Authentication auth = new UsernamePasswordAuthenticationToken(email, credentialsHelper, roles);
            return Mono.just(auth).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }

        return Mono.empty(); // Authentication failed
    }

    private Collection<GrantedAuthority> extractRoles(Claims tokenClaims) {
        List<String> roles = tokenClaims.get("role", List.class);
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
