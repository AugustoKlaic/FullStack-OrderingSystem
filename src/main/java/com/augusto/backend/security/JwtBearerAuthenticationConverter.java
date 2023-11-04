package com.augusto.backend.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwtBearerAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

    private static final String BEARER = "Bearer ";
    private final JwtUtil jwtUtil;

    public JwtBearerAuthenticationConverter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(this::extract) // extract header auth
                .filter(authValue -> authValue != null && authValue.startsWith(BEARER))
                .flatMap(authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length())))
                .flatMap(jwtUtil::check) // validate token
                .flatMap(this::create);
    }

    public Mono<Authentication> create(String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        String subject = claims.getSubject();
        List<String> roles = claims.get("role", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(subject, null, authorities));
    }

    public Mono<String> extract(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
