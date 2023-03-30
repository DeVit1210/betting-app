package com.betting.security.auth.admin;

import com.betting.security.auth.mapping.AdminAuthenticationRequestMapper;
import com.betting.security.auth.mapping.AdminRegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import com.betting.security.auth.responses.ResponseBuilder;
import com.betting.user.admin.Admin;
import com.betting.user.admin.AdminRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class AdminAuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private ResponseBuilder responseBuilder;
    @InjectMocks
    private AdminAuthenticationService authenticationService;
    @Value("${test.username}")
    private String username;
    @Value("${test.password}")
    private String password;
    @Value("${test.adminSecret}")
    private String secretCode;

    @Test
    void testAuthenticateSuccess() {
        AdminAuthenticationRequest request = new AdminAuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        Admin admin = mock(Admin.class);
        when(admin.getPassword()).thenReturn(password);
        when(admin.getSecretCode()).thenReturn(secretCode);
        when(adminRepository.findAdminByUsername(anyString())).thenReturn(Optional.of(admin));
        when(responseBuilder.buildAuthenticationResponse(admin, authenticationService.generateAdminClaims(request)))
                .thenReturn(AuthenticationResponse.builder().authenticationToken(anyString()).build());
        AuthenticationResponse response =
                authenticationService.authenticate(request, new AdminAuthenticationRequestMapper());
        assertNotNull(response.getAuthenticationToken());
    }

    @Test
    void testAuthenticateNotFound() {
        AdminAuthenticationRequest request = new AdminAuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request, new AdminAuthenticationRequestMapper()));
    }
    @Test
    void testRegistration() {
        AdminAuthenticationRequest request = new AdminAuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        AdminRegistrationRequestMapper mapper = new AdminRegistrationRequestMapper();
        Admin admin = mapper.mapFrom(request);
        when(adminRepository.findAdminByUsername(admin.getUsername())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> authenticationService.register(request, mapper));
        verify(adminRepository, times(1)).save(any(Admin.class));
    }
}