package com.betting.exceptions;

public class InvalidRequestParameterException extends RuntimeException {
    public InvalidRequestParameterException(String message) {
        super(message);
    }

    public InvalidRequestParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
