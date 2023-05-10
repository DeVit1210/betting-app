package com.betting.security.auth.confirmation;

import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class ConfirmationTokenServiceTest {
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private ConfirmationToken confirmationToken;
    private String token = UUID.randomUUID().toString();
    @BeforeEach
    void setUp() {
        String username = "mozolden7@gmail.com";
        Player player = Player.builder()
                .username(username)
                .password(passwordEncoder.encode("PASSWORD"))
                .phoneNumber("PHONE_NUMBER")
                .passwordSeries("AB")
                .passwordNumber("1234567")
                .fullName("fullName")
                .isNonLocked(false)
                .isEnabled(false)
                .build();
        playerRepository.save(player);
        confirmationToken = new ConfirmationToken(UUID.randomUUID().toString(), LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), player);
    }

    @AfterEach
    void tearDown() {
        confirmationTokenRepository.deleteAll();
        playerRepository.deleteAll();
    }
    @Test
    void testSaveToken() {
        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(any(ConfirmationToken.class));
        confirmationTokenService.saveToken(confirmationToken);
        Mockito.verify(confirmationTokenRepository, times(1)).save(confirmationToken);
    }

    @Test
    void testGetToken() {
        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(confirmationToken));
        assertTrue(confirmationTokenService.getToken(token).isPresent());
    }

    @Test
    void testUpdateConfirmed() {
        doNothing().when(confirmationTokenRepository).setConfirmed(anyString(), any(LocalDateTime.class));
        confirmationTokenService.updateConfirmed(token);
        verify(confirmationTokenRepository, times(1)).setConfirmed(eq(token), any(LocalDateTime.class));
    }
}