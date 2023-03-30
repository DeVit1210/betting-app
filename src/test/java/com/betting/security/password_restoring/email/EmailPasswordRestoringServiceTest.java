package com.betting.security.password_restoring.email;

import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.security.auth.mail.EmailService;
import com.betting.security.auth.mail.MailType;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.security.auth.validation.TokenValidationResult;
import com.betting.security.auth.validation.TokenValidationService;
import com.betting.security.exceptions.InvalidConfirmationTokenException;
import com.betting.security.exceptions.UserNotFoundException;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class EmailPasswordRestoringServiceTest {
    @InjectMocks
    private EmailPasswordRestoringService restoringService;
    @Mock
    private TokenValidationService tokenValidationService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private PlayerService playerService;
    @Mock
    private EmailService emailService;
    @Mock
    private ResponseBuilder responseBuilder;
    @Value("${test.newPassword}")
    private String newPassword;
    @Value("${test.username}")
    private String username;
    @Value("${test.exception.player-not-found}")
    private String playerNotFoundExceptionMessage;
    private final String token = UUID.randomUUID().toString();
    @Test
    void testChangePasswordSuccess() {
        EmailPasswordChangingRequest request = mock(EmailPasswordChangingRequest.class);
        when(request.getNewPassword()).thenReturn(newPassword);
        when(request.getConfirmationToken()).thenReturn(token);
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn(username);
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getToken()).thenReturn(token);
        when(confirmationToken.getPlayer()).thenReturn(player);
        when(tokenValidationService.getValidConfirmationToken(anyString())).thenReturn(confirmationToken);
        doNothing().when(confirmationTokenService).updateConfirmed(token);
        doNothing().when(playerService).updatePassword(anyString(), anyString());
        when(responseBuilder.buildAuthenticationResponse(any(Player.class)))
                .thenReturn(AuthenticationResponse.builder().authenticationToken("").build());
        AuthenticationResponse response = restoringService.changePassword(request);
        assertNotNull(response.getAuthenticationToken());
    }

    @Test
    void testChangePasswordNotValidToken() {
        EmailPasswordChangingRequest request = mock(EmailPasswordChangingRequest.class);
        when(request.getNewPassword()).thenReturn(newPassword);
        when(request.getConfirmationToken()).thenReturn(token);
        when(tokenValidationService.getValidConfirmationToken(anyString()))
                .thenThrow(new InvalidConfirmationTokenException(TokenValidationResult.anyButSuccess().name()));
        assertThatThrownBy(() -> restoringService.changePassword(request))
                .isInstanceOf(InvalidConfirmationTokenException.class)
                .hasMessageNotContaining(TokenValidationResult.SUCCESS.name());
    }

    @Test
    void testGenerateConfirmationTokenSuccess() throws MessagingException {
        Player player = mock(Player.class);
        when(playerService.loadPlayerByUsername(username)).thenReturn(Optional.of(player));
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString(), any(MailType.class));
        doNothing().when(confirmationTokenService).saveToken(confirmationToken);
        when(responseBuilder.buildResponse(anyString()))
                .thenReturn(AuthenticationResponse.builder().authenticationToken(anyString()).build());
        AuthenticationResponse response = restoringService.generateConfirmationToken(username);
        assertNotNull(response.getAuthenticationToken());
    }
    @Test
    void testGenerateConfirmationTokenWrongUsername() {
        when(playerService.loadPlayerByUsername(username)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> restoringService.generateConfirmationToken(username))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(playerNotFoundExceptionMessage);
    }
}