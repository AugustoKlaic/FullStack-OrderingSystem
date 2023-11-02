package com.augusto.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecretWord;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecretWord.getBytes()))
                .compact();
    }

    private SecretKey getKey(String secret){
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey(jwtSecretWord))
                .build()
                .parseEncryptedClaims(token)
                .getPayload();
    }
}
