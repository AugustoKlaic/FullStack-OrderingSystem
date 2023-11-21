package com.augusto.backend.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class CustomWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        response.beforeCommit(() -> {
            HttpHeaders mutableHeaders = new HttpHeaders();
            if (Objects.equals(exchange.getResponse().getStatusCode(), HttpStatus.CREATED)) {
                mutableHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION);
            }

            if (exchange.getRequest().getPath().value().contains("/login")
                || exchange.getRequest().getPath().value().contains("/token-refresh")) {
                mutableHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
            }

            response.getHeaders().addAll(mutableHeaders);
            return Mono.empty();
        });

        return chain.filter(exchange);
    }
}
