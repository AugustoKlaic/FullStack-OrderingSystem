package com.augusto.backend.service;

import com.augusto.backend.domain.Address;
import com.augusto.backend.domain.Client;
import com.augusto.backend.dto.AddressDto;
import com.augusto.backend.dto.ClientDto;
import com.augusto.backend.dto.CompleteClientDto;
import com.augusto.backend.repository.AddressRespository;
import com.augusto.backend.repository.CityRepository;
import com.augusto.backend.repository.ClientRepository;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AddressRespository addressRespository;
    private final CityRepository cityRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, AddressRespository addressRespository, CityRepository cityRepository) {
        this.clientRepository = clientRepository;
        this.addressRespository = addressRespository;
        this.cityRepository = cityRepository;
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client findById(final Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found for Id: " + id));
    }

    @Transactional
    public Client create(final CompleteClientDto client) {
        Client clientSaved = clientRepository.save(toDomainObject(client));

        Address addressToBeSaved = toDomainObject(client.getAddress());
        addressToBeSaved.setClient(clientSaved);
        addressToBeSaved.setCity(cityRepository.findById(client.getAddress().getCityCode())
                .orElseThrow(() -> new ObjectNotFoundException("Client not found for Id: " + client.getAddress().getCityCode())));
        clientSaved.setAddresses(Set.of(addressRespository.save(addressToBeSaved)));

        return clientSaved;
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

    private Client toDomainObject(CompleteClientDto clientDto) {
        return new Client(clientDto.getName(),
                clientDto.getEmail(),
                clientDto.getNationalIdentity(),
                clientDto.getClientType(),
                clientDto.getTelephones(),
                List.of());
    }

    private Address toDomainObject(AddressDto address) {
        return new Address(address.getStreet(), address.getNumber(), address.getComplement(), address.getNeighborhood(), address.getCep());
    }
}
