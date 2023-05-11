package com.betting.security.auth.player;

import com.betting.security.auth.mail.EmailService;
import com.betting.security.auth.mapping.PlayerAuthenticationRequestMapper;
import com.betting.security.auth.mapping.RegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.test_builder.impl.PlayerBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@SpringBootTest
class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PlayerService playerService;
    @Mock
    private ResponseBuilder responseBuilder;
    @Mock
    private EmailService emailService;
    private final RegistrationRequestMapper registrationRequestMapper = new RegistrationRequestMapper();
    private final PlayerAuthenticationRequestMapper authenticationRequestMapper = new PlayerAuthenticationRequestMapper();
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String username = "mozolden7@gmail.com";

    @Test
    void testAuthenticateSuccess() {
        Player player = PlayerBuilder.aPlayerBuilder().withUsername(username).build();
        PlayerAuthenticationRequest authenticationRequest = new PlayerAuthenticationRequest();
        authenticationRequest.setUsername(player.getUsername());
        authenticationRequest.setPassword(player.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(player.getPassword(), player.getUsername()));
        when(playerService.loadPlayerByUsername(anyString())).thenReturn(Optional.of(player));
        when(responseBuilder.buildAuthenticationResponse(player))
                .thenReturn(AuthenticationResponse.builder().authenticationToken(anyString()).build());
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest, authenticationRequestMapper);
        assertNotNull(response.getAuthenticationToken());
    }

    @Test
    void testAuthenticateNotEnabled() {
        PlayerAuthenticationRequest authenticationRequest = new PlayerAuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword("password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new IllegalStateException());
        assertThrows(IllegalStateException.class, () -> authenticationService.authenticate(authenticationRequest, authenticationRequestMapper));
    }
}