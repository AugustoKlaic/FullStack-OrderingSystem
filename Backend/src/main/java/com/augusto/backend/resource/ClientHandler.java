package com.augusto.backend.resource;

import com.augusto.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> "Rest test"), String.class);
    }

    public Mono<ServerResponse> getClientById(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> clientService.findById(Integer.parseInt(serverRequest.pathVariable("id"))))
                .flatMap(client -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(client))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .bodyValue(e));

    }
}
