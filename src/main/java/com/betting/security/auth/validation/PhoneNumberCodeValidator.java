package com.betting.security.auth.validation;

import com.betting.security.password_restoring.phone_code.PhoneNumberCode;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

import static com.betting.security.auth.validation.CodeValidationResult.*;

public interface PhoneNumberCodeValidator extends Function<PhoneNumberCode, CodeValidationResult> {
    static PhoneNumberCodeValidator isCodeNotUsed() {
        return phoneNumberCode -> Objects.isNull(phoneNumberCode.getConfirmedAt()) ?
                SUCCESS : CODE_HAS_ALREADY_BEEN_USED;
    }

    static PhoneNumberCodeValidator isCodeNonExpired() {
        return phoneNumberCode -> phoneNumberCode.getExpiresAt().isAfter(LocalDateTime.now()) ?
                SUCCESS : CODE_IS_EXPIRED;
    }

    static PhoneNumberCodeValidator isCodeBelongToTheNumber(String phoneNumber) {
        return phoneNumberCode -> phoneNumberCode.getPhoneNumber().equals(phoneNumber) ?
            SUCCESS : CODE_DOES_NOT_BELONG_TO_THIS_NUMBER;
    }

    default PhoneNumberCodeValidator and(PhoneNumberCodeValidator other) {
        return phoneNumberCode -> {
            CodeValidationResult result = this.apply(phoneNumberCode);
            return result.equals(SUCCESS) ? other.apply(phoneNumberCode) : result;
        };
    }
}

