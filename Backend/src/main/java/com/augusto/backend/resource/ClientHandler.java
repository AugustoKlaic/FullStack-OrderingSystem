package com.augusto.backend.resource;

import com.augusto.backend.resource.validator.ErrorClass;
import com.augusto.backend.resource.validator.ValidatorException;
import com.augusto.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ClientHandler {

    private final ClientService clientService;

    @Autowired
    public ClientHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    public Mono<ServerResponse> getClients(ServerRequest serverRequest) {
        return Mono.fromCallable(clientService::findAllClients)
                .flatMap(clients -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(clients));
    }

    public Mono<ServerResponse> getClientById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> clientService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(client -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(client))
                .onErrorResume(this::errorHandler);
    }

    public Mono<ServerResponse> errorHandler(Throwable error) {
        if (error instanceof ValidatorException) {
            return ServerResponse.unprocessableEntity()
                    .bodyValue(new ErrorClass(((ValidatorException) error).getErrorDetail()));
        } else {
            return ServerResponse.badRequest().build();
        }
    }
}
