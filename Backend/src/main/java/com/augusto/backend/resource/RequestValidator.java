package com.augusto.backend.resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

public abstract class RequestValidator<T, U extends Validator> {

    private final Class<T> validationClass;
    private final U validator;

    public RequestValidator(Class<T> validationClass, U validator) {
        this.validationClass = validationClass;
        this.validator = validator;
    }
    
    public final Mono<ServerResponse> validateRequest(final ServerRequest request) {
        return request.bodyToMono(this.validationClass)
                .flatMap(body -> {
                    Errors errors = new BeanPropertyBindingResult(
                            body, this.validationClass.getName());

                    this.validator.validate(body, errors);

                    if (errors == null || errors.getAllErrors().isEmpty()) {
                        return processBody(body, request);
                    } else {
                        return onValidationErrors(errors, body, request);
                    }
                });
    }

    private Mono<ServerResponse> onValidationErrors(Errors errors, T invalidBody, ServerRequest request) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                errors.getAllErrors().toString());
    }

}
