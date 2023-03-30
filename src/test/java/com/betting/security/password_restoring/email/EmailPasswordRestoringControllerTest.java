package com.betting.security.password_restoring.email;

import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.validation.TokenValidationResult;
import com.betting.security.exceptions.InvalidConfirmationTokenException;
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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
class EmailPasswordRestoringControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmailPasswordRestoringService restoringService;
    private final AuthenticationResponse response = new AuthenticationResponse();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${test.authenticationToken}")
    private String authenticationToken;
    @BeforeEach
    void setUp() {
        response.setAuthenticationToken(authenticationToken);
    }
    @Test
    void testChangePasswordSuccess() throws Exception {
        EmailPasswordChangingRequest request = new EmailPasswordChangingRequest();
        when(restoringService.changePassword(request)).thenReturn(response);
        mockMvc.perform(post("/user/change-password/email")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.authenticationToken").value(authenticationToken))
                .andExpect(status().isOk());
    }
    @Test
    void testChangePasswordInvalidToken() throws Exception {
        EmailPasswordChangingRequest request = new EmailPasswordChangingRequest();
        when(restoringService.changePassword(request)).
                thenThrow(new InvalidConfirmationTokenException(TokenValidationResult.anyButSuccess().name()));
        mockMvc.perform(post("/user/change-password/email")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidConfirmationTokenException))
                .andExpect(result -> assertNotEquals(result.getResolvedException().getMessage(), TokenValidationResult.SUCCESS));
    }
}