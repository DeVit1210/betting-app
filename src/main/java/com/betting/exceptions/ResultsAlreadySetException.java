package com.betting.exceptions;

public class ResultsAlreadySetException extends RuntimeException {
    public ResultsAlreadySetException(String message) {
        super(message);
    }

    public ResultsAlreadySetException() {
        super("results already set to this event");
    }
}
