package com.betting.security.auth.validation;

import java.util.Random;

public enum TokenValidationResult {
    SUCCESS,
    TOKEN_DOES_NOT_EXIST,
    EMAIL_IS_ALREADY_CONFIRMED,
    TOKEN_IS_EXPIRED;

    public static TokenValidationResult anyButSuccess() {
        return switch (new Random().nextInt(3)) {
            case 0 -> TOKEN_DOES_NOT_EXIST;
            case 1 -> TOKEN_IS_EXPIRED;
            case 2 -> EMAIL_IS_ALREADY_CONFIRMED;
            default -> throw new IllegalStateException();
        };
    }
}
