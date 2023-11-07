package com.augusto.backend.resource.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ClientCreateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientCreate {
    String message() default "Invalid create Client data.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
