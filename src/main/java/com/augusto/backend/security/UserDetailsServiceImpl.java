package com.augusto.backend.security;

import com.augusto.backend.domain.Client;
import com.augusto.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final ClientRepository clientRepository;

    @Autowired
    public UserDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        return client.<Mono<UserDetails>>map(value ->
                Mono.just(new UserSpringSecurity(value.getId(), value.getEmail(), value.getClientPassword(), value.getClientProfiles())))
                .orElseGet(() -> Mono.error(new UsernameNotFoundException("client Not Found")));
    }
}
