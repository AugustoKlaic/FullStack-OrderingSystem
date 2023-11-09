package com.augusto.backend.resource;

import com.augusto.backend.resource.validator.ErrorClass;
import com.augusto.backend.resource.validator.ValidatorException;
import com.augusto.backend.service.exception.AuthenticationException;
import com.augusto.backend.service.exception.AuthorizationException;
import com.augusto.backend.service.exception.IllegalObjectException;
import com.augusto.backend.service.exception.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ErrorResolver {

    public static Mono<ServerResponse> errorHandler(Throwable error, String domain) {
        if (error instanceof ValidatorException) {
            return ServerResponse.unprocessableEntity()
                    .bodyValue(new ErrorClass(((ValidatorException) error).getErrorDetail()));
        } else if (error instanceof ObjectNotFoundException) {
            return ServerResponse.notFound().build();
        } else if (error instanceof IllegalObjectException) {
            return ServerResponse.badRequest()
                    .bodyValue(new ErrorClass(domain, error.getMessage()));
        } else if (error instanceof DataIntegrityViolationException){
            return ServerResponse.unprocessableEntity()
                    .bodyValue(new ErrorClass(domain, error.getMessage()));
        } else if (error instanceof AuthenticationException) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .bodyValue(new ErrorClass(domain, error.getMessage()));
        } else if(error instanceof AuthorizationException) {
            return ServerResponse.status(HttpStatus.FORBIDDEN)
                    .bodyValue(new ErrorClass(domain, error.getMessage()));
        } else {
            return ServerResponse.badRequest().build();
        }
    }
}
