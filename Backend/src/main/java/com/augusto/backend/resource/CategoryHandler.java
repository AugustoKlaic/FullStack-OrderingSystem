package com.augusto.backend.resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CategoryHandler {

    public Mono<ServerResponse> getCategories(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.fromCallable(() -> "Rest test"), String.class);
    }
}
