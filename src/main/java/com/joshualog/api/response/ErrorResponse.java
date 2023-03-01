package com.joshualog.api.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code; //  400
    private final String message; // 잘못된 요청입니다.
    private final Map<String, String> validation;
    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
//        this.validation = validation;
        this.validation = validation != null ? validation : new HashMap<>();
    }
}
