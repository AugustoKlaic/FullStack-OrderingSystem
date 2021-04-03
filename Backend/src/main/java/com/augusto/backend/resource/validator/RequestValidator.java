package com.augusto.backend.resource.validator;

import com.augusto.backend.resource.exception.WebException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class RequestValidator {

    public static <T> Mono<T> validateRequest(final ServerRequest request, final Class<T> clazz) {
        return request.bodyToMono(clazz)
                .flatMap(body -> {
                    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                    Validator validator = validatorFactory.getValidator();

                    Set<ConstraintViolation<T>> violations = validator.validate(body);

                    if (violations == null || violations.isEmpty()) {
                        return Mono.just(body);
                    } else {
                        return Mono.error(new WebException(
                                HttpStatus.BAD_REQUEST,
                                violations.toString()));
                    }
                });
    }
}
