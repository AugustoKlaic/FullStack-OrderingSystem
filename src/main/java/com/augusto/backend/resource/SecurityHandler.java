package com.augusto.backend.resource;

import com.augusto.backend.dto.CredentialsDto;
import com.augusto.backend.dto.ForgotMyPasswordDto;
import com.augusto.backend.resource.validator.RequestValidator;
import com.augusto.backend.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SecurityHandler {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String AUTH_DOMAIN = "Auth";

    private final SecurityService securityService;
    private final RequestValidator requestValidator;


    @Autowired
    public SecurityHandler(SecurityService securityService, RequestValidator requestValidator) {
        this.securityService = securityService;
        this.requestValidator = requestValidator;
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CredentialsDto.class)
                .map(credentials -> securityService.authenticate(credentials.getEmail(), credentials.getPassword()))
                .flatMap(tokenInfo -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders -> {
                            httpHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
                            httpHeaders.add(HttpHeaders.AUTHORIZATION, (TOKEN_PREFIX + tokenInfo.getToken()));
                        })
                        .bodyValue(tokenInfo))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, AUTH_DOMAIN));
    }

    public Mono<ServerResponse> refreshToken(ServerRequest serverRequest) {
        return securityService.refreshToken()
                .flatMap(tokenInfo -> ServerResponse.noContent()
                        .headers(httpHeaders -> {
                            httpHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
                            httpHeaders.add(HttpHeaders.AUTHORIZATION, (TOKEN_PREFIX + tokenInfo.getToken()));
                        }).build())
                .onErrorResume(e -> ErrorResolver.errorHandler(e, AUTH_DOMAIN));
    }

    public Mono<ServerResponse> forgetPassword(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ForgotMyPasswordDto.class)
                .doOnNext(requestValidator::validateRequest)
                .doOnNext(email -> securityService.forgotPassword(email.getEmail()))
                .flatMap(tokenInfo -> ServerResponse.noContent().build())
                .onErrorResume(e -> ErrorResolver.errorHandler(e, AUTH_DOMAIN));
    }
}
