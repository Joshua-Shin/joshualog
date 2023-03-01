package com.joshualog.api.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public abstract class JologException extends RuntimeException{
    private Map<String, String> validation = new HashMap<>();
    public JologException(String message) {
        super(message);
    }
    public abstract int getStatus();
    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
