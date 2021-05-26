package com.augusto.backend.resource.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestValidator {

    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateRequest(final Object requestObject) {
        final Class<?> target = requestObject.getClass();

        final List<String> errors = this.validator.validate(requestObject)
                .stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        final Map<String, List<String>> errorMap = Map.of(target.getSimpleName(), errors);

        if (!errors.isEmpty()) {
            throw new ValidatorException(errorMap);
        }
    }
}
