package com.augusto.backend.security;

import com.augusto.backend.domain.Client;
import com.augusto.backend.domain.enums.ClientProfileEnum;
import com.augusto.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.RequestPath;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final String CLIENT_URL = "/clients";

    private final ClientService clientService;

    @Autowired
    public CustomAuthorizationManager(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext ctx) {
        return authentication.flatMap(auth -> {
            Client client = clientService.findByEmail(auth.getPrincipal().toString());

            if (client == null) {
                return Mono.just(new AuthorizationDecision(false));
            }

            RequestPath path = ctx.getExchange().getRequest().getPath();

            if (path.value().contains(CLIENT_URL)) {
                if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(ClientProfileEnum.ADMIN.getDescription()))
                        && !client.getId().equals(extractUserIdFromPath(path))) {
                    return Mono.just(new AuthorizationDecision(false));
                } else {
                    return Mono.just(new AuthorizationDecision(true));
                }
            } else {
                return Mono.just(new AuthorizationDecision(true));
            }
        });
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        return ReactiveAuthorizationManager.super.verify(authentication, object);
    }

    private Integer extractUserIdFromPath(RequestPath path) {
        return Integer.valueOf(path.elements().getLast().value());
    }
}
