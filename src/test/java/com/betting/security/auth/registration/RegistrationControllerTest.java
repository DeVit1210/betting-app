package com.betting.security.auth.registration;

import com.betting.security.auth.mapping.RegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.validation.TokenValidationResult;
import com.betting.exceptions.EmailAlreadyTakenException;
import com.betting.exceptions.InvalidConfirmationTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationService registrationService;
    private final AuthenticationResponse response = new AuthenticationResponse();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RegistrationRequest request = new RegistrationRequest();
    @Value("${test.authenticationToken}")
    private String authenticationToken;
    @Value("${test.exception.email-already-taken}")
    private String emailAlreadyTakenExceptionMessage;

    @BeforeEach
    void setUp() {
        response.setAuthenticationToken(authenticationToken);
    }

    @Test
    void testRegistration() throws Exception {
        when(registrationService.register(any(RegistrationRequest.class), any(RegistrationRequestMapper.class)))
                .thenReturn(response);
        mockMvc.perform(post("/user/register")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.authenticationToken").value(authenticationToken))
                .andExpect(status().isOk());
    }

    @Test
    void testRegistrationAlreadySignedUp() throws Exception {
        when(registrationService.register(any(RegistrationRequest.class), any(RegistrationRequestMapper.class)))
                .thenThrow(new EmailAlreadyTakenException(emailAlreadyTakenExceptionMessage));
        mockMvc.perform(post("/user/register")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmailAlreadyTakenException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), emailAlreadyTakenExceptionMessage));
    }

    @Test
    void testConfirmationSuccess() throws Exception {
        when(registrationService.confirm(anyString())).thenReturn(response);
        mockMvc.perform(get("/user/confirm")
                        .param("token", authenticationToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticationToken").value(authenticationToken));
    }

    @Test
    void testConfirmationWrongToken() throws Exception {
        when(registrationService.confirm(anyString()))
                .thenThrow(new InvalidConfirmationTokenException(TokenValidationResult.anyButSuccess().name()));
        mockMvc.perform(get("/user/confirm")
                        .param("token", authenticationToken))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidConfirmationTokenException))
                .andExpect(result -> assertNotEquals(result.getResolvedException().getMessage(),TokenValidationResult.SUCCESS.name()));
    }
}