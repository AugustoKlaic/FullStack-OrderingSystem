package com.augusto.backend.resource;

import com.augusto.backend.dto.CredentialsDto;
import com.augusto.backend.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SecurityHandler {
    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final SecurityService securityService;

    @Autowired
    public SecurityHandler(SecurityService securityService) {
        this.securityService = securityService;
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CredentialsDto.class)
                .map(credentials -> securityService.authenticate(credentials.getEmail(), credentials.getPassword()))
                .flatMap(tokenInfo -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, (TOKEN_PREFIX + tokenInfo.getToken()))
                        .bodyValue(tokenInfo));
    }
}
