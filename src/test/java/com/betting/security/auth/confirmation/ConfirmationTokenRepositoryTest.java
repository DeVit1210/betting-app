package com.betting.security.auth.confirmation;

import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfirmationTokenRepositoryTest {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String testToken = UUID.randomUUID().toString();
    private ConfirmationToken confirmationToken;

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
        confirmationToken = new ConfirmationToken(testToken, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), player);
        confirmationTokenRepository.save(confirmationToken);
    }

    @AfterEach
    void tearDown() {
        confirmationTokenRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    void testFindByToken() {
        Optional<ConfirmationToken> token = confirmationTokenRepository.findByToken(testToken);
        assertTrue(token.isPresent());
    }

    @Test
    void testConfirmed() {
        Optional<LocalDateTime> confirmedAtBefore = Optional.ofNullable(confirmationToken.getConfirmedAt());
        confirmationTokenRepository.setConfirmed(testToken, LocalDateTime.now());
        ConfirmationToken updatedToken = confirmationTokenRepository.findByToken(testToken).get();
        assertTrue(confirmedAtBefore.isEmpty());
        assertNotNull(updatedToken.getConfirmedAt());
    }
}