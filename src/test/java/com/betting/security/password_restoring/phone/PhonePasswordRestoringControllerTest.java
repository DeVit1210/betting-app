package com.betting.security.password_restoring.phone;

import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.validation.CodeValidationResult;
import com.betting.security.exceptions.InvalidPhoneCodeException;
import com.betting.security.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
class PhonePasswordRestoringControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PhonePasswordRestoringService restoringService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${test.phoneNumber}")
    private String phoneNumber;
    @Value("${test.authenticationToken}")
    private String authenticationToken;
    @Value("${test.exception.player-not-found}")
    private String userNotFoundExceptionMessage;

    @Test
    void testGeneratePhoneNumberCodeSuccess() throws Exception {
        when(restoringService.generatePhoneNumberCode(anyString())).thenReturn(ResponseEntity.ok().build());
        mockMvc.perform(get("/user/forgot-password/phone").param("phoneNumber", phoneNumber))
                .andExpect(status().isOk());
    }

    @Test
    void testGeneratePhoneNumberCodeWrongNumber() throws Exception {
        when(restoringService.generatePhoneNumberCode(anyString())).thenReturn(ResponseEntity.badRequest().body(anyString()));
        mockMvc.perform(get("/user/forgot-password/phone").param("phoneNumber", phoneNumber))
                .andExpect(status().isBadRequest());
    }

   @Test
    void testVerifyCodeSuccess() throws Exception {
        CodeVerificationRequest request = new CodeVerificationRequest();
        when(restoringService.verifyPhoneNumberCode(any(CodeVerificationRequest.class)))
                .thenReturn(ResponseEntity.ok().body(phoneNumber));
        mockMvc.perform(post("/user/verify-code")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), phoneNumber));
    }

    @Test
    void testVerifyCodeInvalidCode() throws Exception {
        CodeVerificationRequest request = new CodeVerificationRequest();
        when(restoringService.verifyPhoneNumberCode(any(CodeVerificationRequest.class)))
                .thenThrow(new InvalidPhoneCodeException(CodeValidationResult.anyButSuccess().name()));
        mockMvc.perform(post("/user/verify-code")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidPhoneCodeException))
                .andExpect(result -> assertNotEquals(result.getResolvedException().getMessage(), CodeValidationResult.SUCCESS.name()));
    }

    @Test
     void testChangePasswordSuccess() throws Exception {
        PasswordChangingRequest request = new PasswordChangingRequest();
        when(restoringService.changePassword(any(PasswordChangingRequest.class)))
                .thenReturn(AuthenticationResponse.builder().authenticationToken(authenticationToken).build());
        mockMvc.perform(post("/user/change-password/phone")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticationToken").value(authenticationToken));
    }

    @Test
    void testChangePasswordWrongPhoneNumber() throws Exception {
        PasswordChangingRequest request = new PasswordChangingRequest();
        when(restoringService.changePassword(any(PasswordChangingRequest.class)))
                .thenThrow(new UserNotFoundException(userNotFoundExceptionMessage));
        mockMvc.perform(post("/user/change-password/phone")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), userNotFoundExceptionMessage));
    }
}