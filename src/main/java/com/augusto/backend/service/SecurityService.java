package com.augusto.backend.service;

import com.augusto.backend.domain.Client;
import com.augusto.backend.dto.TokenDto;
import com.augusto.backend.security.JwtUtil;
import com.augusto.backend.service.email.EmailService;
import com.augusto.backend.service.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

@Service
public class SecurityService {

    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final ClientService clientService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final Random random;

    @Autowired
    public SecurityService(ClientService clientService, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.random = new Random();
    }

    public void forgotPassword(String email) {
        Client client = clientService.findByEmail(email);

        String newPassword = generateNewPassword();
        client.setClientPassword(passwordEncoder.encode(newPassword));
        Client updatedClient = clientService.update(client);
        emailService.sendPasswordRecoveryEmail(updatedClient, updatedClient.getClientPassword());
    }

    private String generateNewPassword() {
        char[] chars = new char[10];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = randomCharacter();
        }
        return new String(chars);
    }

    private char randomCharacter() {
        //the numbers present on the "nextInt(x) + y
        // represent the value of it in the unicode table"

        int opt = random.nextInt(3);
        if (opt == 0) { //generate digit
            return (char) (random.nextInt(10) + 48);
        } else if (opt == 1) { //generate upper case letter
            return (char) (random.nextInt(26) + 65);
        } else { //generate lower case letter
            return (char) (random.nextInt(26) + 97);
        }
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
