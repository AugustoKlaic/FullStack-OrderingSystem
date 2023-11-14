package com.augusto.backend.security;

import com.augusto.backend.domain.Client;
import com.augusto.backend.domain.enums.ClientProfileEnum;
import com.augusto.backend.repository.ClientRepository;
import com.augusto.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final ClientRepository clientRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public CustomAuthorizationManager(ClientRepository clientRepository, JwtUtil jwtUtil) {
        this.clientRepository = clientRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext ctx) {
        return authentication.map(auth -> {
            CredentialsHelper credentialsHelper = (CredentialsHelper) auth.getCredentials();

            Optional<Client> client = clientRepository.findByEmail(auth.getPrincipal().toString());

            if (client.isEmpty()) {
                return new AuthorizationDecision(false);
            }

            if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(ClientProfileEnum.ADMIN.getDescription()))
                    && !client.get().getId().equals(Integer.valueOf(credentialsHelper.getClientId()))) {
                return new AuthorizationDecision(false);
            } else {
                return new AuthorizationDecision(jwtUtil.isTokenValid(credentialsHelper.getToken()));
            }
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }
}
