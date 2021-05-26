package com.augusto.backend.resource.validator;

import java.util.List;
import java.util.Map;

public class ErrorClass {

    private Map<String, List<String>> reasons;

    public ErrorClass(Map<String, List<String>> reasons) {
        this.reasons = reasons;
    }

    public Map<String, List<String>> getReasons() {
        return reasons;
    }

    public void setReasons(Map<String, List<String>> reasons) {
        this.reasons = reasons;
    }

}
