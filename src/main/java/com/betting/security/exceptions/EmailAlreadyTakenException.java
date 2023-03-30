package com.betting.security.exceptions;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
    public EmailAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
