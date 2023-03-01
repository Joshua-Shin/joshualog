package com.joshualog.api.exception;

/**
 * status -> 400
 */
public class InvalidException extends JologException{
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidException() {
        super(MESSAGE);
    }
    public InvalidException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }
    @Override
    public int getStatus() {
        return 400;
    }
}
