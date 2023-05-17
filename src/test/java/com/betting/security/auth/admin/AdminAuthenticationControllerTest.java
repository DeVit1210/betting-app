package com.betting.security.auth.admin;

import com.betting.exceptions.EmailAlreadyTakenException;
import com.betting.mapping.AdminAuthenticationRequestMapper;
import com.betting.mapping.AdminRegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:test.properties")
class AdminAuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminAuthenticationService authenticationService;
    @Value("${test.authenticationToken}")
    private String authenticationToken;
    @Value("${test.exception.email-already-taken}")
    private String emailAlreadyTakenExceptionMessage;
    private final AdminAuthenticationRequest request = new AdminAuthenticationRequest();
    private final AuthenticationResponse response = new AuthenticationResponse();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        response.setAuthenticationToken(authenticationToken);
    }

    @Test
    void testAuthenticate() throws Exception {
        when(authenticationService.authenticate(any(AdminAuthenticationRequest.class), any(AdminAuthenticationRequestMapper.class)))
                .thenReturn(response);
        mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.authenticationToken").value(authenticationToken))
                .andExpect(status().isOk());
    }

    @Test
    void testRegistration() throws Exception {
        when(authenticationService.register(any(AdminAuthenticationRequest.class), any(AdminRegistrationRequestMapper.class)))
                .thenReturn(response);
        mockMvc.perform(post("/admin/register")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.authenticationToken").value(authenticationToken))
                .andExpect(status().isOk());
    }

    // TODO: fix with exception handlers
    @Test
    void testRegistrationAlreadyRegistered() throws Exception{
        when(authenticationService.register(any(AdminAuthenticationRequest.class), any(AdminRegistrationRequestMapper.class)))
                .thenThrow(new EmailAlreadyTakenException(emailAlreadyTakenExceptionMessage));
        mockMvc.perform(post("/admin/register")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmailAlreadyTakenException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), emailAlreadyTakenExceptionMessage));
    }
}