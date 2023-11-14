package com.augusto.backend.service;

import com.augusto.backend.domain.Address;
import com.augusto.backend.domain.Client;
import com.augusto.backend.dto.AddressDto;
import com.augusto.backend.dto.ClientDto;
import com.augusto.backend.dto.CompleteClientDto;
import com.augusto.backend.repository.AddressRespository;
import com.augusto.backend.repository.CityRepository;
import com.augusto.backend.service.exception.IllegalObjectException;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Service
public class ClientRepository {

    private final com.augusto.backend.repository.ClientRepository clientRepository;
    private final AddressRespository addressRespository;
    private final CityRepository cityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    @Autowired
    public ClientRepository(com.augusto.backend.repository.ClientRepository clientRepository, AddressRespository addressRespository,
                            CityRepository cityRepository, BCryptPasswordEncoder passwordEncoder, S3Service s3Service) {
        this.clientRepository = clientRepository;
        this.addressRespository = addressRespository;
        this.cityRepository = cityRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client findById(final Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found for Id: " + id));
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found for email: " + email));
    }

    @Transactional
    public Client create(final CompleteClientDto client) {
        Client clientSaved = clientRepository.save(toDomainObject(client));

        Address addressToBeSaved = toDomainObject(client.getAddress());
        addressToBeSaved.setClient(clientSaved);
        addressToBeSaved.setCity(cityRepository.findById(client.getAddress().getCityCode())
                .orElseThrow(() -> new ObjectNotFoundException("City not found for Id: " + client.getAddress().getCityCode())));
        clientSaved.setAddresses(Set.of(addressRespository.save(addressToBeSaved)));

        return clientSaved;
    }

    @Transactional
    public Client update(final ClientDto clientDto, final Integer id) {
        clientRepository.findAlreadyInsertedEmail(clientDto.getEmail(), id)
                .ifPresent(isIllegalEmail -> {
                    if(isIllegalEmail) {
                        throw new IllegalObjectException("Email already in use");
                    }
                });

        final Client toUpdateClient = findById(id);
        toUpdateClient.setName(clientDto.getName());
        toUpdateClient.setEmail(clientDto.getEmail());
        return clientRepository.save(toUpdateClient);
    }

    @Transactional
    public Client update(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Integer deleteById(final Integer id) {
        clientRepository.deleteById(findById(id).getId());
        return id;
    }

    public Mono<URI> uploadProfilePicture(FilePart filePart) {
        return s3Service.uploadFile(filePart)
                .flatMap(uri -> ReactiveSecurityContextHolder.getContext()
                        .flatMap(ctx -> {
                            Integer clientId = Integer.valueOf(ctx.getAuthentication().getCredentials().toString());
                            Client client = this.findById(clientId);

                            client.setClientProfilePictureUrl(uri.toString());
                            return Mono.fromCallable(() -> this.clientRepository.save(client))
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .thenReturn(uri);
                        }));
    }

    private Client toDomainObject(CompleteClientDto clientDto) {
        return new Client(clientDto.getName(),
                clientDto.getEmail(),
                clientDto.getNationalIdentity(),
                clientDto.getClientType(),
                clientDto.getTelephones(),
                List.of(),
                passwordEncoder.encode(clientDto.getClientPassword()));
    }

    private Address toDomainObject(AddressDto address) {
        return new Address(address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCep());
    }
}
