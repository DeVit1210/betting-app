package com.betting.security.auth.admin;

import com.betting.security.auth.mapping.AdminAuthenticationRequestMapper;
import com.betting.security.auth.mapping.AdminRegistrationRequestMapper;
import com.betting.security.auth.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthenticationController {
    private final AdminAuthenticationService authenticationService;
    private final BeanFactory beanFactory;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AdminAuthenticationRequest request) {
        AdminAuthenticationRequestMapper mapper = beanFactory.getBean(AdminAuthenticationRequestMapper.class);
        return ResponseEntity.ok(authenticationService.authenticate(request, mapper));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AdminAuthenticationRequest request) {
        AdminRegistrationRequestMapper mapper = beanFactory.getBean(AdminRegistrationRequestMapper.class);
        return ResponseEntity.ok(authenticationService.register(request,mapper));
    }
}