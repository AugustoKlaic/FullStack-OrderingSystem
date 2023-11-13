package com.augusto.backend.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class JwtWebFilter implements WebFilter {

    private static final String BEARER = "Bearer ";
    private final JwtUtil jwtUtil;

    public JwtWebFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain chain) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(this::extract) // extract header auth
                .filter(authValue -> authValue != null && authValue.startsWith(BEARER))
                .flatMap(authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length())))
                .flatMap(jwtUtil::check) // validate token
                .flatMap(this::create)
                .flatMap(auth -> chain.filter(serverWebExchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)))
                .switchIfEmpty(chain.filter(serverWebExchange));
    }

    public Mono<Authentication> create(String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        String subject = claims.getSubject();
        String clientId = claims.get("clientId").toString();
        List<String> roles = claims.get("role", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(subject, clientId, authorities));
    }

    public Mono<String> extract(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
