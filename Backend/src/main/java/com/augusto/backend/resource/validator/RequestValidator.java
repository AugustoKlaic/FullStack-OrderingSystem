package com.augusto.backend.resource.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestValidator {

    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateRequest(final Object requestObject) {
        List<String> validationErrors = this.validator.validate(requestObject)
                .stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        if (!validationErrors.isEmpty()) {
            throw new ValidationException();
        }
    }
}
