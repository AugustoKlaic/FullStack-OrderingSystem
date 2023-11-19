package com.augusto.backend.resource;

import com.augusto.backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AddressHandler {

    private static final String ADDRESS_DOMAIN = "Address";

    private final AddressService addressService;

    @Autowired
    public AddressHandler(AddressService addressService) {
        this.addressService = addressService;
    }

    public Mono<ServerResponse> getStates(ServerRequest serverRequest) {
        return Mono.fromCallable(addressService::findStates)
                .flatMap(states -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(states));
    }

    public Mono<ServerResponse> getCities(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> addressService.findCitiesByState(Integer.valueOf(serverRequest.pathVariable("stateId"))))
                .flatMap(cities -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(cities))
                .onErrorResume(e -> ErrorResolver.errorHandler(e, ADDRESS_DOMAIN));
    }


}
