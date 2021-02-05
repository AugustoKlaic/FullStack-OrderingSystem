package com.augusto.backend.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> categoryRouter(CategoryHandler categoryHandler) {
        return route().path("/categories", builder -> builder
                .nest(accept(MediaType.APPLICATION_JSON), uriBuilder -> uriBuilder
                        .GET("", categoryHandler::getCategories)
                        .GET("/{id}", categoryHandler::getCategoriesById)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> clientRouter(ClientHandler clientHandler) {
        return route().path("/client", builder -> builder
                .nest(accept(MediaType.APPLICATION_JSON), uriBuilder -> uriBuilder
                        .GET("", clientHandler::getClients)
                        .GET("/{id}", clientHandler::getClientById )))
                .build();
    }

}
