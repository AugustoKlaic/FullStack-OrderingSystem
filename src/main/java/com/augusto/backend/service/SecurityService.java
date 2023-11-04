package com.augusto.backend.service;

import com.augusto.backend.domain.Client;
import com.augusto.backend.security.JwtUtil;
import com.augusto.backend.service.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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


    public String authenticate(String email, String password) {
        Client client = clientService.findByEmail(email);

        if (passwordEncoder.matches(password, client.getClientPassword())) {
            return jwtUtil.generateToken(client.getEmail());
        } else {
            throw new AuthenticationException("Invalid client password!");
        }
    }
}
