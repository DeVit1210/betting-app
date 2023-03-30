package com.betting.security.password_restoring.phone_code;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class PhoneNumberCodeServiceTest {
    @InjectMocks
    private PhoneNumberCodeService phoneNumberCodeService;
    @Mock
    private PhoneNumberCodeRepository phoneNumberCodeRepository;
    @Value("${test.phoneCode}")
    private String code;

    @Test
    void testFindByCodeSuccess() {
        PhoneNumberCode phoneNumberCode = mock(PhoneNumberCode.class);
        when(phoneNumberCodeRepository.findByCode(anyString())).thenReturn(Optional.of(phoneNumberCode));
        assertDoesNotThrow(() -> phoneNumberCodeService.findByCode(code));
    }

    @Test
    void testFindByCodeNotFound() {
        when(phoneNumberCodeRepository.findByCode(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalAccessError.class, () -> phoneNumberCodeService.findByCode(code));
    }

    @Test
    void testVerifyNumber() {
        doNothing().when(phoneNumberCodeRepository).confirm(any(String.class), any(LocalDateTime.class));
        phoneNumberCodeService.verifyNumber(code);
        verify(phoneNumberCodeRepository, times(1)).confirm(code, LocalDateTime.now());
    }
}