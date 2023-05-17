package com.betting.security.auth.registration;

import com.betting.mapping.RegistrationRequestMapper;
import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.security.auth.mail.EmailService;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.security.auth.validation.TokenValidationService;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@SpringBootTest
class RegistrationServiceTest {
    @InjectMocks
    private RegistrationService registrationService;
    @Mock
    private TokenValidationService tokenValidationService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private PlayerService playerService;
    @Mock
    private ResponseBuilder responseBuilder;
    @Mock
    private EmailService emailService;
    private final String token = UUID.randomUUID().toString();
    @Test
    void testConfirm() {
        // player mock
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn("username");
        when(player.getPassword()).thenReturn("password");
        // confirmation token mock
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getToken()).thenReturn(token);
        when(confirmationToken.getPlayer()).thenReturn(player);
        // service mocks
        when(tokenValidationService.getValidConfirmationToken(token)).thenReturn(confirmationToken);
        doNothing().when(confirmationTokenService).updateConfirmed(token);
        doNothing().when(playerService).enablePlayer(player);
        // responseBuilder mock
        when(responseBuilder.buildAuthenticationResponse(player))
                .thenReturn(AuthenticationResponse.builder().authenticationToken(anyString()).build());
        // test
        AuthenticationResponse response = registrationService.confirm(token);
        //assert
        assertDoesNotThrow(() -> registrationService.confirm(token));
        assertNotNull(response.getAuthenticationToken());
    }

    @Test
    void testRegistration() throws MessagingException {
        RegistrationRequest request = new RegistrationRequest();
        RegistrationRequestMapper mapper = new RegistrationRequestMapper();
        String confirmationToken = UUID.randomUUID().toString();
        when(playerService.signPlayerUp(any(Player.class))).thenReturn(confirmationToken);
        when(responseBuilder.buildResponse(anyString()))
                .thenReturn(AuthenticationResponse.builder().authenticationToken(confirmationToken).build());
        AuthenticationResponse response = registrationService.register(request, mapper);
        assertNotNull(response.getAuthenticationToken());
        assertEquals(response.getAuthenticationToken(), confirmationToken);
    }
}