package com.joshualog.api.exception;

/**
 * status -> 404
 */
public class PostNotFound extends JologException{
    private static final String MESSAGE = "존재하지 않는 글입니다.";
    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 404;
    }
}
