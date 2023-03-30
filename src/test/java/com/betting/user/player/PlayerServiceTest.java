package com.betting.user.player;

import com.betting.security.auth.confirmation.ConfirmationToken;
import com.betting.security.auth.confirmation.ConfirmationTokenService;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import com.betting.user.player.PlayerService;
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
    private String PASSWORD;
    @Value("${test.phoneNumber}")
    private String PHONE_NUMBER;
    @Value("${test.username}")
    private String USERNAME;
    @BeforeEach
    void setUp() {
        player = Player.builder()
                .username(USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .phoneNumber(PHONE_NUMBER)
                .passwordSeries("AB")
                .passwordNumber("1234567")
                .fullName("fullName")
                .isNonLocked(false)
                .isEnabled(false)
                .build();
        playerRepository.save(player);
    }

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
    }

    @Test
    void testLoadPlayerByUsername() {
        when(playerRepository.findPlayerByUsername(USERNAME)).thenReturn(Optional.of(player));
        Optional<Player> find = playerService.loadPlayerByUsername(USERNAME);
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
       playerService.enablePlayer("mozolden7@gmail.com");
       verify(playerRepository, times(1)).enablePlayer(USERNAME);
       verify(playerRepository, times(1)).unlockPlayer(USERNAME);
    }

    @Test
    void testFindPlayerByPhoneNumberNotFound() {
        when(playerRepository.findPlayerByPhoneNumber(AdditionalMatchers.not(ArgumentMatchers.eq(PHONE_NUMBER)))).thenReturn(Optional.empty());
        assertThat(playerService.findPlayerByPhoneNumber(PHONE_NUMBER)).isEmpty();
    }

    @Test
    void testFindPlayerByPhoneNumber() {
        when(playerRepository.findPlayerByPhoneNumber(PHONE_NUMBER)).thenReturn(Optional.of(player));
        assertThat(playerService.findPlayerByPhoneNumber(PHONE_NUMBER)).isNotEmpty();
    }

    @Test
    void updatePassword() {
        doNothing().when(playerRepository).updatePassword(any(String.class), any(String.class));
        String encodedPassword = passwordEncoder.encode(PASSWORD);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        playerService.updatePassword(USERNAME, encodedPassword);
        Mockito.verify(playerRepository, times(1)).updatePassword(USERNAME, encodedPassword);
    }
}