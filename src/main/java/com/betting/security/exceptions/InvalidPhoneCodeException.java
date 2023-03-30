package com.betting.security.exceptions;

public class InvalidPhoneCodeException extends RuntimeException {
    public InvalidPhoneCodeException() {
        super();
    }

    public InvalidPhoneCodeException(String message) {
        super(message);
    }

    public InvalidPhoneCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
