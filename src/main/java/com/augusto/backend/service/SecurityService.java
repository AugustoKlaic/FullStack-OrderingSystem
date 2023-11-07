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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SecurityService {

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (passwordEncoder.matches(password, client.getClientPassword())) {
            String token = jwtUtil.generateToken(client);
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            return new TokenDto(
                    client.getId(),
                    token,
                    sdf.format(claims.getIssuedAt()),
                    sdf.format(claims.getExpiration()));
        } else {
            throw new AuthenticationException("Invalid client password!");
        }
    }
}
