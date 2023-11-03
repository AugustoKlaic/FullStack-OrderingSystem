package com.augusto.backend.service;

import com.augusto.backend.domain.Client;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public Client authenticate(String email, String password) {
        return new Client();
    }
}
