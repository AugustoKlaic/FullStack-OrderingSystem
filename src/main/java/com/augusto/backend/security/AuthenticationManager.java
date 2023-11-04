package com.augusto.backend.security;

import com.augusto.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final ClientService clientService;

    @Autowired
    public AuthenticationManager(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(clientService.findByEmail(authentication.getName()))
                .map(user -> authentication);
    }
}
