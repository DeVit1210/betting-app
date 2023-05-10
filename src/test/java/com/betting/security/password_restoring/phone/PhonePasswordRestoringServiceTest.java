package com.betting.security.password_restoring.phone;

import com.betting.exceptions.UserNotFoundException;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.security.auth.validation.CodeValidationResult;
import com.betting.security.auth.validation.CodeValidationService;
import com.betting.exceptions.InvalidPhoneCodeException;
import com.betting.security.password_restoring.phone_code.PhoneNumberCode;
import com.betting.security.password_restoring.phone_code.PhoneNumberCodeService;
import com.betting.security.password_restoring.sms.SmsService;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class PhonePasswordRestoringServiceTest {
    @InjectMocks
    private PhonePasswordRestoringService restoringService;
    @Mock
    private PlayerService playerService;
    @Mock
    private ResponseBuilder responseBuilder;
    @Mock
    private PhoneNumberCodeService phoneNumberCodeService;
    @Mock
    private SmsService smsService;
    @Mock
    private CodeValidationService codeValidationService;
    @Value("${test.phoneNumber}")
    private String phoneNumber;
    @Value("${test.username}")
    private String username;
    @Value("${test.phoneCode}")
    private String code;
    @Value("${test.newPassword}")
    private String newPassword;

    @Test
    void testGeneratePhoneNumberCode() {
        String correctNumber = phoneNumber.substring(1);
        Player player = mock(Player.class);
        when(playerService.findPlayerByPhoneNumber(anyString())).thenReturn(Optional.of(player));
        when(phoneNumberCodeService.generateCode(player)).thenReturn(code);
        doNothing().when(smsService).sendSMS(anyString(), anyString());
        ResponseEntity<String> response = restoringService.generatePhoneNumberCode(correctNumber);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGeneratePhoneNumberCodeWrongNumber() {
        when(playerService.findPlayerByPhoneNumber(anyString())).thenReturn(Optional.empty());
        ResponseEntity<String> response = restoringService.generatePhoneNumberCode(phoneNumber);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("there is no player with this phone number", response.getBody());
    }

    @Test
    void testVerifyPhoneCode() {
        PhoneNumberCode phoneNumberCode = mock(PhoneNumberCode.class);
        when(codeValidationService.getValidPhoneCode(code, phoneNumber)).thenReturn(phoneNumberCode);
        doNothing().when(phoneNumberCodeService).verifyNumber(code);
        CodeVerificationRequest request = mock(CodeVerificationRequest.class);
        when(request.getPhoneNumber()).thenReturn(phoneNumber);
        when(request.getVerificationCode()).thenReturn(code);
        ResponseEntity<String> response = restoringService.verifyPhoneNumberCode(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(phoneNumber, response.getBody());
    }

    @Test
    void testVerifyPhoneCodeInvalidCode() {
        CodeVerificationRequest request = mock(CodeVerificationRequest.class);
        when(request.getPhoneNumber()).thenReturn(phoneNumber);
        when(request.getVerificationCode()).thenReturn(code);
        when(codeValidationService.getValidPhoneCode(code, phoneNumber))
                .thenThrow(new InvalidPhoneCodeException(CodeValidationResult.anyButSuccess().name()));
        assertThatThrownBy(() -> restoringService.verifyPhoneNumberCode(request))
                .isInstanceOf(InvalidPhoneCodeException.class)
                .hasMessageNotContaining(CodeValidationResult.SUCCESS.name());
    }

    @Test
    void testChangePasswordSuccess() {
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn(username);
        PasswordChangingRequest request = mock(PasswordChangingRequest.class);
        when(request.getNewPassword()).thenReturn(newPassword);
        when(request.getPhoneNumber()).thenReturn(phoneNumber);
        when(playerService.findPlayerByPhoneNumber(anyString())).thenReturn(Optional.of(player));
        doNothing().when(playerService).updatePassword(username, newPassword);
        when(responseBuilder.buildAuthenticationResponse(any(Player.class)))
                .thenReturn(AuthenticationResponse.builder().authenticationToken("").build());
        AuthenticationResponse response = restoringService.changePassword(request);
        assertNotNull(response.getAuthenticationToken());
    }

    @Test
    void testChangePasswordNotFound() {
        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn(username);
        PasswordChangingRequest request = mock(PasswordChangingRequest.class);
        when(playerService.findPlayerByPhoneNumber(anyString())).thenThrow(IllegalStateException.class);
        assertThrows(UserNotFoundException.class, () -> restoringService.changePassword(request));
    }

}