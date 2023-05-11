package com.betting.user.player;

import com.betting.test_builder.impl.PlayerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class PlayerRepositoryTest {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${test.username}")
    private String username;
    @Value("${test.phoneNumber}")
    private String phoneNumber;
    @Value("${test.password}")
    private String password;
    @BeforeEach
    public void setUp() {
        Player player = PlayerBuilder.aPlayerBuilder()
                .withUsername(username)
                .withPassword(passwordEncoder.encode(password))
                .withPhoneNumber(phoneNumber)
                .build();
        playerRepository.save(player);
    }

    @AfterEach
    public void tearDown() {
        playerRepository.deleteAll();
    }
    @Test
    void testFindPlayerByUsername() {
        Optional<Player> player = playerRepository.findPlayerByUsername(username);
        assertThat(player).isNotEmpty();
    }

    @Test
    void testFindPlayerByUsernameNotFound() {
        String wrongUsername = username + "wrong";
        Optional<Player> player = playerRepository.findPlayerByUsername(wrongUsername);
        assertThat(player).isEmpty();
    }

    @Test
    void testFindPlayerByPhoneNumber() {
        Optional<Player> player = playerRepository.findPlayerByPhoneNumber(phoneNumber);
        assertThat(player).isNotEmpty();
    }

    @Test
    void testFindPlayerByPhoneNumberNotFound() {
        String wrongPhoneNumber = "+375331234567";
        Optional<Player> player = playerRepository.findPlayerByPhoneNumber(wrongPhoneNumber);
        assertThat(player).isEmpty();
    }

    @Test
    void testEnablePlayer() {
        playerRepository.enablePlayer(username);
        Optional<Player> player = playerRepository.findPlayerByUsername(username);
        assertThat(player).isNotEmpty();
        assertTrue(player.get().isEnabled());
    }

    @Test
    void testUnlockPlayer() {
        playerRepository.unlockPlayer(username);
        Optional<Player> player = playerRepository.findPlayerByUsername(username);
        assertThat(player).isNotEmpty();
        assertTrue(player.get().isAccountNonLocked());
    }

    @ParameterizedTest
    @ValueSource(strings = {"pass-pass", "password", "Pass"})
    void updatePassword(String newPassword) {
        Player playerWithOldPassword = playerRepository.findPlayerByUsername(username).get();
        playerRepository.updatePassword(username, newPassword);
        Player playerWithNewPassword = playerRepository.findPlayerByUsername(username).get();
        assertNotEquals(playerWithOldPassword.getPassword(), playerWithNewPassword.getPassword());
    }
}