package com.augusto.backend.resource.validator;

import com.augusto.backend.domain.enums.ClientTypeEnum;
import com.augusto.backend.dto.CompleteClientDto;
import com.augusto.backend.repository.ClientRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClientValidator implements ConstraintValidator<ClientInfo, CompleteClientDto> {

    private final ClientRepository clientRepository;

    public ClientValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void initialize(ClientInfo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CompleteClientDto value, ConstraintValidatorContext context) {
        List<String> errors = new ArrayList<>();

        if (ClientTypeEnum.LEGAL_PERSON.equals(value.getClientType()) && !isValidCNPJ(value.getNationalIdentity())) {
            errors.add("CNPJ is invalid.");
        }


        if (ClientTypeEnum.PHYSIC_PERSON.equals(value.getClientType()) && !isValidCPF(value.getNationalIdentity())) {
            errors.add("CPF is invalid.");
        }

        clientRepository.findAlreadyInsertedEmail(value.getEmail())
                .ifPresent(email -> errors.add("Email already registered."));

        for (String error : errors) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error)
                    .addPropertyNode(CompleteClientDto.class.toString())
                    .addConstraintViolation();
        }

        return errors.isEmpty();
    }

    // CPF
    private static final int[] WEIGHT_SSN = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    // CNPJ
    private static final int[] WEIGHT_TFN = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static int sum(int[] weight, char[] numbers, int length) {
        if (length <= 0) return 0;
        final int nIdx = length - 1;
        final int wIdx = weight.length > numbers.length ? length : nIdx;
        return (sum(weight, numbers, nIdx) + Character.getNumericValue(numbers[nIdx]) * weight[wIdx]);
    }

    private static int calculate(final String document, final int[] weight) {
        final char[] numbers = document.toCharArray();
        int sum = sum(weight, numbers, numbers.length);
        sum = 11 - (sum % 11);
        return sum > 9 ? 0 : sum;
    }

    private static boolean check(String tfn, int length, int[] weight) {
        final String number = tfn.substring(0, length);
        final int digit1 = calculate(number, weight);
        final int digit2 = calculate(number + digit1, weight);
        return tfn.equals(number + digit1 + digit2);
    }

    /**
     * Valida CPF
     */
    public static boolean isValidCPF(String ssn) {
        if (ssn == null || !ssn.matches("\\d{11}") || ssn.matches(ssn.charAt(0) + "{11}")) return false;
        return check(ssn, 9, WEIGHT_SSN);
    }

    /**
     * Valida CNPJ
     */
    public static boolean isValidCNPJ(String tfn) {
        if (tfn == null || !tfn.matches("\\d{14}")) return false;
        return check(tfn, 12, WEIGHT_TFN);
    }
}
