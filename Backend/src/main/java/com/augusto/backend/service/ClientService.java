package com.augusto.backend.service;

import com.augusto.backend.domain.Category;
import com.augusto.backend.domain.Client;
import com.augusto.backend.dto.CategoryDto;
import com.augusto.backend.dto.ClientDto;
import com.augusto.backend.repository.ClientRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found for Id: " + id));
    }

    @Transactional
    public Client update(final ClientDto clientDto) {
        final Client toUpdateClient = findById(clientDto.getId());
        toUpdateClient.setName(clientDto.getName());
        toUpdateClient.setEmail(clientDto.getEmail());
        return clientRepository.save(toUpdateClient);
    }

    @Transactional
    public Integer deleteById(final Integer id) {
        clientRepository.deleteById(findById(id).getId());
        return id;
    }

    private Client toDomainObject(ClientDto clientDto) {
        return new Client(clientDto.getId(), clientDto.getName(), clientDto.getEmail());
    }
}
