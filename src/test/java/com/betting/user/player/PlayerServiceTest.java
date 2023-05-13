package com.betting.user.player;

import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.test_builder.impl.PlayerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class PlayerServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private PlayerService playerService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    private Player player;
    @Value("${test.password}")
    private String password;
    @Value("${test.phoneNumber}")
    private String phoneNumber;
    @Value("${test.username}")
    private String username;
    @BeforeEach
    void setUp() {
        player = PlayerBuilder.aPlayerBuilder()
                .withUsername(username)
                .withPassword(passwordEncoder.encode(password))
                .withPhoneNumber(phoneNumber)
                .build();
        playerRepository.save(player);
    }

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
    }

    @Test
    void testLoadPlayerByUsername() {
        when(playerRepository.findPlayerByUsername(username)).thenReturn(Optional.of(player));
        Optional<Player> find = playerService.loadPlayerByUsername(username);
        assertThat(find).isNotEmpty();
    }

    // TODO
    @Test
    void testSignPlayerUp() {
        when(playerRepository.findPlayerByUsername(anyString())).thenReturn(Optional.empty());
        ConfirmationToken token = mock(ConfirmationToken.class);
        doNothing().when(confirmationTokenService).saveToken(token);
        assertDoesNotThrow(() -> playerService.signPlayerUp(player));
    }

    @Test
    void testSignPlayerUpAlreadyRegistered() {
        when(playerRepository.findPlayerByUsername(anyString())).thenReturn(Optional.of(player));
        ConfirmationToken token = mock(ConfirmationToken.class);
        doNothing().when(confirmationTokenService).saveToken(token);
        assertThatThrownBy(() -> playerService.signPlayerUp(player))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("email is already taken");
    }

    @Test
    void testEnablePlayer() {
        doNothing().when(playerRepository).enablePlayer(anyString());
        doNothing().when(playerRepository).unlockPlayer(anyString());
        playerService.enablePlayer(player);
        verify(playerRepository, times(1)).enablePlayer(username);
        verify(playerRepository, times(1)).unlockPlayer(username);
    }

    @Test
    void testFindPlayerByPhoneNumberNotFound() {
        when(playerRepository.findPlayerByPhoneNumber(AdditionalMatchers.not(ArgumentMatchers.eq(phoneNumber)))).thenReturn(Optional.empty());
        assertThat(playerService.findPlayerByPhoneNumber(phoneNumber)).isEmpty();
    }

    @Test
    void testFindPlayerByPhoneNumber() {
        when(playerRepository.findPlayerByPhoneNumber(phoneNumber)).thenReturn(Optional.of(player));
        assertThat(playerService.findPlayerByPhoneNumber(phoneNumber)).isNotEmpty();
    }

    @Test
    void updatePassword() {
        doNothing().when(playerRepository).updatePassword(any(String.class), any(String.class));
        String encodedPassword = passwordEncoder.encode(password);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        playerService.updatePassword(username, encodedPassword);
        Mockito.verify(playerRepository, times(1)).updatePassword(username, encodedPassword);
    }
}