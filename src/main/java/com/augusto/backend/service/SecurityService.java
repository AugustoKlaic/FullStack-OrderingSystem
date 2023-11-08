package com.augusto.backend.service;

import com.augusto.backend.domain.Client;
import com.augusto.backend.dto.TokenDto;
import com.augusto.backend.security.JwtUtil;
import com.augusto.backend.service.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Service
public class SecurityService {

    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final ClientService clientService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityService(ClientService clientService, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public TokenDto authenticate(String email, String password) {
        Client client = clientService.findByEmail(email);

        if (passwordEncoder.matches(password, client.getClientPassword())) {
            return buildToken(client);
        } else {
            throw new AuthenticationException("Invalid client password!");
        }
    }

    public TokenDto refreshToken(String email) {
        Client client = clientService.findByEmail(email);
        return buildToken(client);
    }

    private TokenDto buildToken(Client client) {
        String token = jwtUtil.generateToken(client);
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        return new TokenDto(
                client.getId(),
                token,
                sdf.format(claims.getIssuedAt()),
                sdf.format(claims.getExpiration()));
    }
}
