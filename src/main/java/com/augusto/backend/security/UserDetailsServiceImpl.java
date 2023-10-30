package com.augusto.backend.security;

import com.augusto.backend.domain.Client;
import com.augusto.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final ClientService clientService;

    @Autowired
    public UserDetailsServiceImpl(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        Client client = clientService.findByEmail(email);
        return Mono.just(new UserSpringSecurity(client.getId(), client.getEmail(), client.getClientPassword(), client.getClientProfiles()));
    }
}
