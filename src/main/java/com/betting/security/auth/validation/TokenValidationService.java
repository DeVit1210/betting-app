package com.betting.security.auth.validation;

import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.security.auth.validation.ConfirmationTokenValidator;
import com.betting.security.auth.validation.TokenValidationResult;
import com.betting.security.exceptions.InvalidConfirmationTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if(result.equals(TokenValidationResult.SUCCESS)) {
            return confirmationToken.orElse(null);
        } else throw new InvalidConfirmationTokenException(result.name());
    }
}
