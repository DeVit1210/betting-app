package com.betting.security.auth.validation;

import java.util.Random;

public enum CodeValidationResult {
    SUCCESS,
    CODE_HAS_ALREADY_BEEN_USED,
    CODE_IS_EXPIRED,
    CODE_DOES_NOT_BELONG_TO_THIS_NUMBER;

    public static CodeValidationResult anyButSuccess() {
        return switch (new Random().nextInt(3)) {
            case 0 -> CODE_IS_EXPIRED;
            case 1 -> CODE_HAS_ALREADY_BEEN_USED;
            case 2 -> CODE_DOES_NOT_BELONG_TO_THIS_NUMBER;
            default -> throw new IllegalStateException();
        };
    }
}
