package com.betting.security.auth.validation;

import com.betting.security.auth.confirmation.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.betting.security.auth.validation.TokenValidationResult.*;


// Combinator pattern
public interface ConfirmationTokenValidator extends Function<Optional<ConfirmationToken>, TokenValidationResult> {
    static ConfirmationTokenValidator isTokenExists() {
        return confirmationToken -> confirmationToken.isPresent() ?
                SUCCESS : TOKEN_DOES_NOT_EXIST;
    }

    static ConfirmationTokenValidator isEmailNonConfirmed() {
        return confirmationToken -> confirmationToken
                .map(token -> Objects.isNull(token.getConfirmedAt()) ? SUCCESS : EMAIL_IS_ALREADY_CONFIRMED)
                .orElse(TOKEN_DOES_NOT_EXIST);
    }

    static ConfirmationTokenValidator isTokenNonExpired() {
        return confirmationToken -> confirmationToken
                .map(token -> token.getExpiresAt().isAfter(LocalDateTime.now()) ? SUCCESS : TOKEN_IS_EXPIRED)
                .orElse(TOKEN_DOES_NOT_EXIST);
    }

    default ConfirmationTokenValidator and(ConfirmationTokenValidator other) {
        return confirmationToken -> {
            TokenValidationResult result = this.apply(confirmationToken);
            return result.equals(SUCCESS) ? other.apply(confirmationToken) : result;
        };
    }
}
