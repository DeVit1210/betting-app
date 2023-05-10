package com.betting.security.auth.validation;

import com.betting.events.util.ThrowableUtils;
import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.exceptions.InvalidConfirmationTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.betting.security.auth.validation.TokenValidationResult.*;

@Service
@RequiredArgsConstructor
public class TokenValidationService {
    private final ConfirmationTokenService confirmationTokenService;
    public ConfirmationToken getValidConfirmationToken(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.getToken(token);
        TokenValidationResult result = ConfirmationTokenValidator.isTokenExists()
                .and(ConfirmationTokenValidator.isTokenNonExpired())
                .and(ConfirmationTokenValidator.isEmailNonConfirmed())
                .apply(confirmationToken);
        // TODO: add custom exception
        ThrowableUtils.trueOrElseThrow(res -> res.equals(SUCCESS), result, new InvalidConfirmationTokenException(result.name()));
        return confirmationToken.orElse(null);
    }
}
