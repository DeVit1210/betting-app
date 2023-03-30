package com.betting.security.exceptions;

public class InvalidConfirmationTokenException extends RuntimeException {
    public InvalidConfirmationTokenException() {
        super();
    }

    public InvalidConfirmationTokenException(String message) {
        super(message);
    }

    public InvalidConfirmationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
