package com.augusto.backend.resource.validator;

import com.augusto.backend.dto.ClientDto;
import com.augusto.backend.dto.CompleteClientDto;
import com.augusto.backend.repository.ClientRepository;
import org.springframework.web.reactive.function.server.ServerRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClientUpdateValidator implements ConstraintValidator<ClientUpdate, ClientDto> {

    private final ClientRepository clientRepository;

    public ClientUpdateValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void initialize(ClientUpdate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ClientDto value, ConstraintValidatorContext context) {
        List<String> errors = new ArrayList<>();

        clientRepository.findAlreadyInsertedEmail(value.getEmail(), 1)
                .ifPresent(email -> errors.add("Email already registered."));

        for (String error : errors) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error)
                    .addPropertyNode(CompleteClientDto.class.toString())
                    .addConstraintViolation();
        }

        return errors.isEmpty();
    }
}
