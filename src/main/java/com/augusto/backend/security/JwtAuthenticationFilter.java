package com.augusto.backend.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements ServerAuthenticationConverter {

    private static final String BEARER = "Bearer ";
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(this::extract) // extract header auth
                .filter(authValue -> authValue != null && authValue.startsWith(BEARER))
                .flatMap(authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length())))
                .flatMap(jwtUtil::check) // validate token
                .switchIfEmpty(Mono.empty())
                .map(this::createAuthentication)
                .flatMap(auth -> Mono.just(auth).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));

    }

    public Authentication createAuthentication(String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        String subject = claims.getSubject();
        String clientId = claims.get("clientId").toString();
        List<String> roles = claims.get("role", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(subject, new CredentialsHelper(clientId, token), authorities);
    }

    public Mono<String> extract(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
