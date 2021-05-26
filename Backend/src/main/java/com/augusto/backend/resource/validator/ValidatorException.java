package com.augusto.backend.resource.validator;

import java.util.List;
import java.util.Map;

public class ValidatorException extends RuntimeException {

    private Map<String, List<String>> errorDetail;

    public ValidatorException(Map<String, List<String>> errorDetail) {
        this.errorDetail = errorDetail;
    }

    public Map<String, List<String>> getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(Map<String, List<String>> errorDetail) {
        this.errorDetail = errorDetail;
    }
}
