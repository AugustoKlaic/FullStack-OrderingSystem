package com.augusto.backend.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.util.function.Function;

@Component
public class RequestValidationHandler {
    private final Validator validator;

    @Autowired
    public RequestValidationHandler(Validator validator) {
        this.validator = validator;
    }

    public <BODY> Mono<ServerResponse> requireValidBody(Function<Mono<BODY>, Mono<ServerResponse>> block,
                                                        ServerRequest serverRequest, Class<BODY> bodyClass) {

        return serverRequest.bodyToMono(bodyClass).flatMap(body -> {
            return validator.validate(body).isEmpty() ? block.apply(Mono.just(body))
                    : ServerResponse.unprocessableEntity().build();
        });

    }
}
