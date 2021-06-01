package com.augusto.backend.service;

import com.augusto.backend.domain.Client;
import com.augusto.backend.repository.ClientRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAllClients(){
        return clientRepository.findAll();
    }

    public Client findById(final Integer id) {
        return clientRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Client not found for Id: " + id));
    }
}
