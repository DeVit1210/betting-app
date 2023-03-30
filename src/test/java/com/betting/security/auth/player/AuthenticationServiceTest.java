package com.betting.security.auth.player;
import com.betting.security.auth.mail.EmailService;
import com.betting.security.auth.mapping.PlayerAuthenticationRequestMapper;
import com.betting.security.auth.mapping.RegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private RegistrationRequestMapper registrationRequestMapper = new RegistrationRequestMapper();
    private PlayerAuthenticationRequestMapper authenticationRequestMapper = new PlayerAuthenticationRequestMapper();
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Player player;
    @BeforeEach
    void setUp() {
        player = Player.builder()
                .username("username")
                .password(passwordEncoder.encode("password"))
                .phoneNumber("PHONE_NUMBER")
                .passwordSeries("AB")
                .passwordNumber("1234567")
                .fullName("fullName")
                .isNonLocked(false)
                .isEnabled(false)
                .build();
    }

    private final String username = "mozolden7@gmail.com";
    @Test
    void testAuthenticateSuccess() {
        PlayerAuthenticationRequest authenticationRequest = new PlayerAuthenticationRequest();
        authenticationRequest.setUsername("username");
        authenticationRequest.setPassword("password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("password", "username"));
        when(playerService.loadPlayerByUsername(anyString())).thenReturn(Optional.of(player));
        when(responseBuilder.buildAuthenticationResponse(player))
                .thenReturn(AuthenticationResponse.builder().authenticationToken(anyString()).build());
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest, authenticationRequestMapper);
        assertNotNull(response.getAuthenticationToken());
    }

    @Test
    void testAuthenticateNotEnabled() {
        PlayerAuthenticationRequest authenticationRequest = new PlayerAuthenticationRequest();
        authenticationRequest.setUsername("username");
        authenticationRequest.setPassword("password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new IllegalStateException());
        assertThrows(IllegalStateException.class, () -> authenticationService.authenticate(authenticationRequest, authenticationRequestMapper));
    }
}