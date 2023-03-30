package com.betting.security.password_restoring.phone_code;

import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class PhoneNumberCodeRepositoryTest {
    @Autowired
    private PhoneNumberCodeRepository phoneNumberCodeRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${test.phoneCode}")
    private String code;
    @Value("${test.password}")
    private String password;
    @Value("${test.username}")
    private String username;
    @Value("${test.phoneNumber}")
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        Player player = Player.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .passwordSeries("AB")
                .passwordNumber("1234567")
                .fullName("fullName")
                .isNonLocked(false)
                .isEnabled(false)
                .build();
        playerRepository.save(player);
        PhoneNumberCode phoneNumberCode = new PhoneNumberCode(
                code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), player);
        phoneNumberCodeRepository.save(phoneNumberCode);
    }

    @AfterEach
    void tearDown() {
        phoneNumberCodeRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    void testFindByCodeSuccess() {
        Optional<PhoneNumberCode> phoneNumberCode = phoneNumberCodeRepository.findByCode(code);
        assertThat(phoneNumberCode).isNotEmpty();
    }

    @Test
    void testFindByCodeNotFound() {
        Optional<PhoneNumberCode> phoneNumberCode = phoneNumberCodeRepository.findByCode("");
        assertThat(phoneNumberCode).isEmpty();
    }

    @Test
    void testUpdateConfirmed() {
        Optional<PhoneNumberCode> codeBefore = phoneNumberCodeRepository.findByCode(code);
        phoneNumberCodeRepository.confirm(code, LocalDateTime.now());
        Optional<PhoneNumberCode> codeAfter = phoneNumberCodeRepository.findByCode(code);
        assertThat(codeBefore).isNotEmpty();
        assertThat(codeAfter).isNotEmpty();
        assertNull(codeBefore.get().getConfirmedAt());
        assertNotNull(codeAfter.get().getConfirmedAt());
    }
}