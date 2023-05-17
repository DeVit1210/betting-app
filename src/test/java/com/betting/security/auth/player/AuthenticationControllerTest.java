package com.betting.security.auth.player;

import com.betting.mapping.PlayerAuthenticationRequestMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:test.properties")
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    private final PlayerAuthenticationRequest request = new PlayerAuthenticationRequest();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    @Value("${test.username}")
    private String username;
    @Value("${test.password}")
    private String password;
    @Value("${test.authenticationToken}")
    private String authenticationToken;

    @BeforeEach
    void setUp() {
        request.setPassword(password);
        request.setUsername(username);
        authenticationResponse.setAuthenticationToken(authenticationToken);
    }

    @Test
    void testAuthenticatePlayer() throws Exception {
        when(authenticationService.authenticate(any(PlayerAuthenticationRequest.class), any(PlayerAuthenticationRequestMapper.class)))
                .thenReturn(authenticationResponse);
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.authenticationToken").value(authenticationToken))
                .andExpect(status().isOk());
    }
}