package com.betting.security.auth.validation;

import com.betting.exceptions.InvalidPhoneCodeException;
import com.betting.security.password_restoring.phone_code.PhoneNumberCode;
import com.betting.security.password_restoring.phone_code.PhoneNumberCodeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class CodeValidationServiceTest {
    @InjectMocks
    private CodeValidationService codeValidationService;
    @Mock
    private PhoneNumberCodeService phoneNumberCodeService;
    @Value("${test.phoneCode}")
    private String code;
    @Value("${test.phoneNumber}")
    private String phoneNumber;

    @Test
    void testGetValidPhoneCodeWhenCodeExpired() {
        PhoneNumberCode phoneNumberCode = mock(PhoneNumberCode.class);
        when(phoneNumberCode.getConfirmedAt()).thenReturn(null);
        when(phoneNumberCode.getExpiresAt()).thenReturn(LocalDateTime.now());
        when(phoneNumberCode.getPhoneNumber()).thenReturn(phoneNumber);
        when(phoneNumberCodeService.findByCode(anyString())).thenReturn(phoneNumberCode);
        assertThatThrownBy(() -> codeValidationService.getValidPhoneCode(code, phoneNumber))
                .isInstanceOf(InvalidPhoneCodeException.class)
                .hasMessage(CodeValidationResult.CODE_IS_EXPIRED.name());
    }

    @Test
    void testGetValidPhoneCodeWhenCodeIsUsed() {
        PhoneNumberCode phoneNumberCode = mock(PhoneNumberCode.class);
        when(phoneNumberCode.getConfirmedAt()).thenReturn(LocalDateTime.now());
        when(phoneNumberCode.getExpiresAt()).thenReturn(LocalDateTime.MAX);
        when(phoneNumberCode.getPhoneNumber()).thenReturn(phoneNumber);
        when(phoneNumberCodeService.findByCode(anyString())).thenReturn(phoneNumberCode);
        assertThatThrownBy(() -> codeValidationService.getValidPhoneCode(code, phoneNumber))
                .isInstanceOf(InvalidPhoneCodeException.class)
                .hasMessage(CodeValidationResult.CODE_HAS_ALREADY_BEEN_USED.name());
    }

    @Test
    void testGetValidPhoneCodeWhenCodeDoesNotMatchPhoneNumber() {
        PhoneNumberCode phoneNumberCode = mock(PhoneNumberCode.class);
        when(phoneNumberCode.getConfirmedAt()).thenReturn(null);
        when(phoneNumberCode.getExpiresAt()).thenReturn(LocalDateTime.MAX);
        when(phoneNumberCode.getPhoneNumber()).thenReturn("");
        when(phoneNumberCodeService.findByCode(anyString())).thenReturn(phoneNumberCode);
        assertThatThrownBy(() -> codeValidationService.getValidPhoneCode(code, phoneNumber))
                .isInstanceOf(InvalidPhoneCodeException.class)
                .hasMessage(CodeValidationResult.CODE_DOES_NOT_BELONG_TO_THIS_NUMBER.name());
    }

    @Test
    void testGetValidPhoneCodeSuccess() {
        PhoneNumberCode phoneNumberCode = mock(PhoneNumberCode.class);
        when(phoneNumberCode.getConfirmedAt()).thenReturn(null);
        when(phoneNumberCode.getExpiresAt()).thenReturn(LocalDateTime.MAX);
        when(phoneNumberCode.getPhoneNumber()).thenReturn(phoneNumber);
        when(phoneNumberCodeService.findByCode(anyString())).thenReturn(phoneNumberCode);
        assertDoesNotThrow(() -> codeValidationService.getValidPhoneCode(code, phoneNumber));
    }
}