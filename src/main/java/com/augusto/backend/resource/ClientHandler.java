package com.augusto.backend.resource;

import com.augusto.backend.dto.ClientDto;
import com.augusto.backend.dto.CompleteClientDto;
import com.augusto.backend.resource.validator.ErrorClass;
import com.augusto.backend.resource.validator.RequestValidator;
import com.augusto.backend.resource.validator.ValidatorException;
import com.augusto.backend.service.ClientService;
import com.augusto.backend.service.exception.IllegalObjectException;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ClientHandler {

    private static final String CLIENT_DOMAIN = "Client";

    private final ClientService clientService;
    private final RequestValidator requestValidator;

    @Autowired
    public ClientHandler(ClientService clientService, RequestValidator requestValidator) {
        this.clientService = clientService;
        this.requestValidator = requestValidator;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<ServerResponse> getClients(ServerRequest serverRequest) {
        return Mono.fromCallable(clientService::findAllClients)
                .flatMap(clients -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(clients));
    }

    public Mono<ServerResponse> getClientById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> clientService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(client -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(client))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, CLIENT_DOMAIN));
    }

    public Mono<ServerResponse> createClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CompleteClientDto.class)
                .doOnNext(requestValidator::validateRequest)
                .map(clientService::create)
                .flatMap(updatedCategory -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(updatedCategory))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, CLIENT_DOMAIN));
    }

    public Mono<ServerResponse> updateClient(ServerRequest serverRequest) {
        Integer clientId = Integer.parseInt(serverRequest.pathVariable("id"));

        return serverRequest.bodyToMono(ClientDto.class)
                .doOnNext(requestValidator::validateRequest)
                .map(client -> clientService.update(client, clientId))
                .flatMap(updatedCategory -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(updatedCategory))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, CLIENT_DOMAIN));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<ServerResponse> deleteClientById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> clientService.deleteById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(categoryId -> ServerResponse.ok().bodyValue(categoryId))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, CLIENT_DOMAIN));
    }
}
