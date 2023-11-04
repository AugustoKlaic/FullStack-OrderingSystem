package com.augusto.backend.security;

import com.augusto.backend.domain.Client;
import com.augusto.backend.service.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecretWord;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public Mono<String> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new AuthenticationException("Invalid token")));
    }

    public String generateToken(Client client) {
        return Jwts.builder()
                .subject(client.getEmail())
                .claim("role", client.getClientProfiles())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecretWord.getBytes()))
                .compact();
    }

    private String verify(String token) {
        var claims = getAllClaimsFromToken(token);
        final Date expiration = claims.getExpiration();

        if (expiration.before(new Date()))
            throw new RuntimeException("Token expired");

        return token;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey(jwtSecretWord))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
