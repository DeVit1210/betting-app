package com.betting.security.auth.validation;

import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.exceptions.InvalidConfirmationTokenException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest
class TokenValidationServiceTest {
    @InjectMocks
    private TokenValidationService tokenValidationService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    private final String token = UUID.randomUUID().toString();
    @Test
    void testGetValidConfirmationTokenWhenTokenNotFound() {
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tokenValidationService.getValidConfirmationToken(token))
                .isInstanceOf(InvalidConfirmationTokenException.class)
                .hasMessage(TokenValidationResult.TOKEN_DOES_NOT_EXIST.name());
    }

    @Test
    void testGetValidConfirmationTokenWhenTokenWasExpired() {
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getExpiresAt()).thenReturn(LocalDateTime.now());
        when(confirmationToken.getConfirmedAt()).thenReturn(null);
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.of(confirmationToken));
        assertThatThrownBy(() -> tokenValidationService.getValidConfirmationToken(token))
                .isInstanceOf(InvalidConfirmationTokenException.class)
                .hasMessage(TokenValidationResult.TOKEN_IS_EXPIRED.name());
    }

    @Test
    void testGetValidConfirmationTokenWhenEmailIsAlreadyConfirmed() {
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getExpiresAt()).thenReturn(LocalDateTime.MAX);
        when(confirmationToken.getConfirmedAt()).thenReturn(LocalDateTime.now());
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.of(confirmationToken));
        assertThatThrownBy(() -> tokenValidationService.getValidConfirmationToken(token))
                .isInstanceOf(InvalidConfirmationTokenException.class)
                .hasMessage(TokenValidationResult.EMAIL_IS_ALREADY_CONFIRMED.name());
    }

    @Test
    void testGetValidConfirmationTokenSuccess() {
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getExpiresAt()).thenReturn(LocalDateTime.MAX);
        when(confirmationToken.getConfirmedAt()).thenReturn(null);
        when(confirmationTokenService.getToken(anyString())).thenReturn(Optional.of(confirmationToken));
        assertDoesNotThrow(() -> tokenValidationService.getValidConfirmationToken(token));
    }
}